<template>
  <v-card class="table">
    <v-data-table
      :headers="headers"
      :items="discussions"
      :search="search"
      multi-sort
      :mobile-breakpoint="0"
      :items-per-page="15"
      :footer-props="{ itemsPerPageOptions: [15, 30, 50, 100] }"
    >
      <template v-slot:top>
        <v-card-title>
          <v-text-field
            v-model="search"
            append-icon="search"
            label="Search"
            class="mx-2"
          />
        </v-card-title>
      </template>

      <template v-slot:item.questionTitle="{ item }">
        <v-chip small>
          <span>{{ item.questionTitle }}</span>
        </v-chip>
      </template>

      <template v-slot:item.title="{ item }">
        <v-chip @click="seeDiscussion(item)" small>
          <span>{{ item.title }}</span>
        </v-chip>
      </template>

      <template v-slot:item.status="{ item }">
        <v-chip :color="getStatusColor(item)" small>
          <span>{{ item.postsDto[item.postsDto.length - 1].status }}</span>
        </v-chip>
      </template>

      <template v-slot:item.creationDate="{ item }">
        {{ item.postsDto[0].creationDate }}
      </template>

      <template v-slot:item.action="{ item }">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon small class="mr-2" v-on="on" @click="seeDiscussion(item)"
              >visibility</v-icon
            >
          </template>
          <span>Show Doubt</span>
        </v-tooltip>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              small
              class="mr-2"
              v-on="on"
              @click="deleteItem(item)"
              color="red"
              data-cy="deleteQuestion"
              >delete</v-icon
            >
          </template>
          <span>Delete Question</span>
        </v-tooltip>
      </template>
    </v-data-table>
    <see-discussion-dialog
      v-if="discussion"
      v-model="seeDiscussionDialog"
      :discussion="discussion"
      :id="discussion.id"
      v-on:see-discussion="onSeeDiscussion"
      v-on:close-dialog="onCloseDialog"
    />
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import Discussion from '@/models/management/Discussion';
import SeeDiscussionDialog from '@/views/student/SeeDiscussionDialog.vue';
@Component({
  components: {
    'see-discussion-dialog': SeeDiscussionDialog
  }
})
export default class DoubtView extends Vue {
  discussions: Discussion[] = [];
  discussion: Discussion | null = null;
  seeDiscussionDialog: boolean = false;
  search: string = '';
  headers: object = [
    { text: 'QuestionTitle', value: 'questionTitle', align: 'left' },
    { text: 'Title', value: 'title', align: 'center' },
    { text: 'Status', value: 'status', align: 'center' },
    { text: 'Creation Date', value: 'creationDate', align: 'center' },
    { text: 'Actions', value: 'action', align: 'center', sortable: false }
  ];

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.discussions = await RemoteServices.getDiscussions();
      console.log(this.discussions);
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  getStatusColor(discussion: Discussion) {
    status = discussion.postsDto[discussion.postsDto.length - 1].status;
    if (status === 'UNSOLVED') return 'red';
    else return 'green';
  }

  seeDiscussion(currentDiscussion: Discussion): void {
    console.log(currentDiscussion);
    this.discussion = currentDiscussion;
    this.seeDiscussionDialog = true;
  }
  async onSeeDiscussion() {
    this.seeDiscussionDialog = false;
    this.discussion = null;
  }
  onCloseDialog() {
    this.seeDiscussionDialog = false;
    this.discussion = null;
  }
}
</script>

<style lang="scss" scoped>
.container {
  max-width: 1000px;
  margin-left: auto;
  margin-right: auto;
  padding-left: 10px;
  padding-right: 10px;
  h2 {
    font-size: 26px;
    margin: 20px 0;
    text-align: center;
    small {
      font-size: 0.5em;
    }
  }
  ul {
    overflow: hidden;
    padding: 0 5px;
    li {
      border-radius: 3px;
      padding: 15px 10px;
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
    }
    .list-header {
      background-color: #1976d2;
      color: white;
      font-size: 14px;
      text-transform: uppercase;
      letter-spacing: 0.03em;
      text-align: center;
    }
    .col {
      width: 25%;
    }
    .last-col {
      max-width: 50px !important;
    }
    .list-row {
      background-color: #ffffff;
      cursor: pointer;
      box-shadow: 0 0 9px 0 rgba(0, 0, 0, 0.1);
    }
    .list-row:hover {
      background-color: #c8c8c8;
    }
  }
}
</style>
