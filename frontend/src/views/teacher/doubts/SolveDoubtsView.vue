<template>
  <v-card class="table">
    <v-data-table
      :headers="headers"
      :items="doubts"
      :search="search"
      disable-pagination
      :mobile-breakpoint="0"
      multi-sort
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
        </v-card-title>
      </template>

      <template v-slot:item.action="{ item }">
        <v-btn
          v-if="!isSolved(item)"
          class="ma-2"
          color="green"
          data-cy="createButton"
          dark
          @click="solve(item)"
          >Solve
          <v-icon dark right>mdi-checkbox-marked-circle</v-icon>
        </v-btn>
        <v-btn
          class="ma-2"

          color="blue darken-3"
          data-cy="detailButton"
          dark
          @click="details(item)"
          >Details
          <v-icon dark right>fas fa-edit</v-icon>
        </v-btn>
      </template>

      <template v-slot:item.status="{ item }">
        <v-chip
          class="ma-2"
          v-if="isSolved(item)"
          color="blue-grey lighten-2"
          text-color="white"
          >{{ item.status }}</v-chip
        >
        <v-chip
          class="ma-2"
          v-if="!isSolved(item)"
          color="red"
          text-color="white"
          >{{ item.status }}</v-chip
        >
      </template>
    </v-data-table>

    <create-clarification-dialog
      v-if="currentDoubt"
      v-model="createClarificationDialog"
      :doubt="currentDoubt"
      v-on:new-clarification="onSolvedDoubt"
      v-on:close-dialog="onCloseDialog"
      :creating="creating"
    />
  </v-card>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Doubt from '@/models/management/Doubt';
import RemoteServices from '@/services/RemoteServices';
import CreateClarificationDialog from '@/views/teacher/doubts/CreateClarificationDialog.vue';

@Component({
  components: {
    'create-clarification-dialog': CreateClarificationDialog
  }
})
export default class SolveDoubtsView extends Vue {
  doubts: Doubt[] = [];
  createClarificationDialog: boolean = false;
  creating: boolean = false;
  search: string = '';
  currentDoubt: Doubt | null = null;
  headers: object = [
    {
      text: 'Question Title',
      value: 'questionTitle',
      align: 'center',
      width: '10%'
    },
    {
      text: 'Author',
      value: 'author',
      align: 'center',
      width: '10%'
    },
    {
      text: 'Status',
      value: 'status',
      align: 'center',
      width: '10%'
    },
    {
      text: '',
      value: 'action',
      align: 'center',
      sortable: false,
      width: '10%'
    }
  ];

  async solve(doubt: Doubt) {
    this.currentDoubt = new Doubt(doubt);
    this.createClarificationDialog = true;
    this.creating = true;
  }

  details(doubt: Doubt) {
    this.currentDoubt = new Doubt(doubt);
    this.createClarificationDialog = true;
    this.creating = false;
  }

  onCloseDialog() {
    this.createClarificationDialog = false;
    this.currentDoubt = null;
  }

  isSolved(doubt: Doubt) {
    return doubt.status == 'SOLVED';
  }

  async onSolvedDoubt() {
    this.createClarificationDialog = false;
    this.currentDoubt = null;
    await this.$store.dispatch('loading');
    try {
      this.doubts = await RemoteServices.manageDoubts();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.doubts = await RemoteServices.manageDoubts();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
}
</script>

<style lang="scss" scoped></style>
