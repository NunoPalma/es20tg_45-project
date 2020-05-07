<template>
  <div>
    <template>
      <div class="container">
        <div class="stats-container">
          <div class="items">
            <div class="icon-wrapper" ref="tournaments">
              <animated-number :number="numberOfParticipatedTournaments" />
            </div>
            <div class="project-name">
              <p>Total Participated Tournaments</p>
            </div>
          </div>
      </div>
     </div>
    </template>
    <v-card class="table" data-cy="tournamentsList">
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
                  v-on:click="onEnrolledButtonClicked(item)"
                  :loading="item.isLoading"
                  :class="isEnrolled(item.tournamentId) ? 'green' : 'red'"
                  align="left"
                  x-small
                  fab="true"
                  :data-cy="item.name"
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
  </div>
</template>


<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Course from '@/models/user/Course';
import Tournament from '@/models/management/Tournament';
import RemoteServices from '@/services/RemoteServices';
import EditTournamentDialog from '@/views/student/EditTournamentDialog.vue';
import StudentTournamentStats from '@/models/statement/StudentTournamentStats';
import AnimatedNumber from '@/components/AnimatedNumber.vue';

@Component({
  components: {
    'edit-tournament-dialog': EditTournamentDialog,
    AnimatedNumber
  }
})
export default class TournamentsView extends Vue {
  tournaments: Tournament[] = [];
  currentCourse: Course | null = null;
  editTournamentDialog: boolean = false;
  search: string = '';
  studentTournamentStats!: StudentTournamentStats;
  numberOfParticipatedTournaments: number = 0;
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
      this.studentTournamentStats = await RemoteServices.getStudentTournamentStats();
      this.numberOfParticipatedTournaments = this.studentTournamentStats.amountOfParticipatedTournaments;
      console.log(this.studentTournamentStats.amountOfParticipatedTournaments);
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

<style lang="scss" scoped>
  .stats-container {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
    align-items: stretch;
    align-content: center;
    height: 100%;

    .items {
      background-color: rgba(255, 255, 255, 0.75);
      color: #1976d2;
      border-radius: 5px;
      flex-basis: 25%;
      margin: 20px;
      cursor: pointer;
      transition: all 0.6s;
    }
  }

  .icon-wrapper,
  .project-name {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .icon-wrapper {
    font-size: 100px;
    transform: translateY(0px);
    transition: all 0.6s;
  }

  .icon-wrapper {
    align-self: end;
  }

  .project-name {
    align-self: start;
  }
  .project-name p {
    font-size: 24px;
    font-weight: bold;
    letter-spacing: 2px;
    transform: translateY(0px);
    transition: all 0.5s;
  }

  .items:hover {
    border: 3px solid black;

    & .project-name p {
      transform: translateY(-10px);
    }
    & .icon-wrapper i {
      transform: translateY(5px);
    }
  }
</style>