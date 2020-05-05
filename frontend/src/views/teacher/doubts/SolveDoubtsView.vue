<template>
  <v-card class="table">
    <v-data-table
      :headers="headers"
      :items="discussions"
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
          >SOLVED</v-chip
        >
        <v-chip
          class="ma-2"
          v-if="!isSolved(item)"
          color="red"
          text-color="white"
          >UNSOLVED</v-chip
        >
      </template>
      <template v-slot:item.visibility="{ item }">
        <v-select
          v-model="item.visibility"
          :items="visibilityList"
          dense
          data-cy="visButton"
          @change="changeVisibility(item.id, item.visibility)"
        >
          <template v-slot:selection="{ item }">
            <v-chip :color="getStatusColor(item)" height="1px">
              <span>{{ item }}</span>
            </v-chip>
          </template>
        </v-select>
      </template>
    </v-data-table>

    <create-clarification-dialog
      v-if="currentDiscussion"
      v-model="createClarificationDialog"
      :discussion="currentDiscussion"
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
import { ToggleButton } from 'vue-js-toggle-button';
import Discussion from '@/models/management/Discussion';

@Component({
  components: {
    'create-clarification-dialog': CreateClarificationDialog,
    ToggleButton: ToggleButton
  }
})

export default class SolveDoubtsView extends Vue {
  discussions: Discussion[] = [];
  createClarificationDialog: boolean = false;
  creating: boolean = false;
  search: string = '';
  currentDiscussion: Discussion | null = null;
  visibilityList = ['PRIVATE', 'PUBLIC'];
  headers: object = [
    {
      text: 'Visibility',
      value: 'visibility',
      align: 'center',
      sortable: false,
      width: '0.1%'
    },
    {
      text: 'Question Title',
      value: 'questionTitle',
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

  onCloseDialog() {
    this.createClarificationDialog = false;
  }

  async solve(discussion: Discussion) {
    this.currentDiscussion = new Discussion(discussion);
    this.createClarificationDialog = true;
    this.creating = true;
  }

  async details(discussion: Discussion) {
    this.currentDiscussion = new Discussion(discussion);
    this.createClarificationDialog = true;
    this.creating = false;
  }

  isSolved(discussion: Discussion) {
    let list = discussion.postsDto;
    return list.length != 0 ? list[list.length - 1].status == 'SOLVED' : false;
  }

  getStatusColor(status: string) {
    if (status === 'PUBLIC') return 'green accent-3';
    return 'blue-grey lighten-4';
  }

  async onSolvedDoubt() {
    this.createClarificationDialog = false;
    await this.$store.dispatch('loading');
    try {
      this.discussions = await RemoteServices.manageDiscussions();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.discussions = await RemoteServices.manageDiscussions();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async changeVisibility(discussionId: number, status: string) {
    try {
      await RemoteServices.changeVisibility(discussionId, status);
      let disc = this.discussions.find(
        discussion => discussion.id === discussionId
      );
      if (disc) {
        disc.visibility = status;
      }
    } catch (error) {
      await this.$store.dispatch('error', error + 'aaa');
    }
  }
}
</script>

<style lang="scss" scoped></style>
