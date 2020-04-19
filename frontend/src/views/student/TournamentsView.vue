<template>
  <v-card class="table">
    <v-data-table
      :headers="headers"
      :items="tournaments"
      :search="search"
      disable-pagination
      :hide-default-footer="false"
      :mobile-breakpoint="0"
      multi-sort
      no-data-text="No available tournaments"
      loading-text="Loading tournaments"
    >
      <template v-slot:top>
        <v-card-title>
          <v-text-field
            v-model="search"
            append-icon="search"
            label="Search"
            class="mx-2"
          />
          <v-spacer />
          <v-btn
            color="primary"
            dark
            @click="newTournament"
            data-cy="createButton"
            >New Tournament</v-btn
          >
        </v-card-title>
      </template>

      <template v-slot:item.name="{ item }">
        <v-layout align="left">
          {{ item.name }}
        </v-layout>
      </template>

      <template v-slot:item.status="{ item }">
        <v-layout align="left">
          {{ item.state }}
        </v-layout>
      </template>

      <template v-slot:item.creator="{ item }">
        <v-layout align="left">
          {{ item.creator.name }}
        </v-layout>
      </template>

      <template v-slot:item.startDate="{ item }">
        <v-layout align="left">
          {{ item.startDate }}
        </v-layout>
      </template>

      <template v-slot:item.endDate="{ item }">
        <v-layout align="left">
          {{ item.endDate }}
        </v-layout>
      </template>

      <template v-slot:item.enrolled="{ item }">
        <v-btn
          :id="item.tournamentId"
          v-on:click="onEnrolledButtonClicked(item)"
          :loading="item.isLoading"
          :class="isEnrolled(item.tournamentId) ? 'green' : 'red'"
          align="left"
          x-small
          fab="true"
        >
        </v-btn>
      </template>
    </v-data-table>

    <edit-tournament-dialog
      v-if="currentCourse"
      v-model="editTournamentDialog"
      :course="currentCourse"
      v-on:new-course="onCreateTournament"
      v-on:close-dialog="onCloseDialog"
    />
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Course from '@/models/user/Course';
import Tournament from '@/models/management/Tournament';
import RemoteServices from '@/services/RemoteServices';
import EditTournamentDialog from '@/views/student/EditTournamentDialog.vue';
@Component({
  components: {
    'edit-tournament-dialog': EditTournamentDialog
  }
})
export default class TournamentsView extends Vue {
  tournaments: Tournament[] = [];
  currentCourse: Course | null = null;
  editTournamentDialog: boolean = false;
  search: string = '';
  headers: object = [
    {
      text: 'Creator',
      value: 'creator',
      align: 'left',
      width: '10%'
    },
    { text: 'Name', value: 'name', align: 'left', width: '10%' },
    {
      text: 'Start Date',
      value: 'startDate',
      align: 'left',
      width: '10%'
    },
    {
      text: 'End Date',
      value: 'endDate',
      align: 'left',
      width: '10%'
    },
    {
      text: 'Status',
      value: 'status',
      align: 'left',
      width: '10%'
    },
    {
      text: 'Enrolled',
      value: 'enrolled',
      align: 'left',
      sortable: false,
      width: '10%'
    },
  ];


  async created() {
    await this.$store.dispatch('loading');
    try {
      this.tournaments = await RemoteServices.getAvailableTournaments();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  newTournament() {
    this.currentCourse = new Course();
    this.editTournamentDialog = true;
  }

  async onCreateTournament(tournament: Tournament) {
    this.tournaments.unshift(tournament);
    this.editTournamentDialog = false;
    this.currentCourse = null;
  }

  onCloseDialog() {
    this.editTournamentDialog = false;
    this.currentCourse = null;
  }

  isEnrolled(tournamentId: number) {
    return this.$store.getters.getUser.tournaments.includes(tournamentId);
  }

  onEnrolledButtonClicked(tournament: Tournament) {
    if (this.isEnrolled(tournament.tournamentId)) return;

    tournament.isLoading = true;
    RemoteServices.enrollStudentInTournament(tournament.tournamentId);

    let _this = this;
    setTimeout(function () {
      tournament.isLoading = false;
      _this.$store.getters.getUser.tournaments.push(tournament.tournamentId);
    }, 1500);
  }
}
</script>

<style lang="scss" scoped></style>