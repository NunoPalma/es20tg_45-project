<template>
  <v-card class="table">
    <v-data-table
      :headers="headers"
      :items="doubts"
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
        <v-chip @click="seeDoubt(item)" small>
          <span>{{ item.title }}</span>
        </v-chip>
      </template>

      <template v-slot:item.status="{ item }">
        <v-chip :color="getStatusColor(item.status)" small>
          <span>{{ item.status }}</span>
        </v-chip>
      </template>

      <template v-slot:item.creationDate="{ item }">
        {{ item.creationDate }}
      </template>

      <template v-slot:item.action="{ item }">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon small class="mr-2" v-on="on" @click="seeDoubt(item)"
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
    <see-doubt-dialog
      v-if="doubt"
      v-model="seeDoubtDialog"
      :doubt="doubt"
      v-on:see-doubt="onSeeDoubt"
      v-on:close-dialog="onCloseDialog"
    />
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import Doubt from '@/models/management/Doubt';
import SeeDoubtDialog from '@/views/student/SeeDoubtDialog.vue';

@Component({
  components: {
    'see-doubt-dialog': SeeDoubtDialog
  }
})
export default class DoubtView extends Vue {
  doubts: Doubt[] = [];
  doubt: Doubt | null = null;
  seeDoubtDialog: boolean = false;
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
      this.doubts = await RemoteServices.getDoubts();
      console.log(this.doubts);

    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  getStatusColor(status: string) {
    if (status === 'UNSOLVED') return 'red';
    else return 'green';
  }

  seeDoubt(currentDoubt: Doubt): void {
    console.log(currentDoubt);
    this.doubt = currentDoubt;
    this.doubt.isNew = false;
    this.seeDoubtDialog = true;
  }

  async onSeeDoubt() {
    this.seeDoubtDialog = false;
    this.doubt = null;
  }

  onCloseDialog() {
    this.seeDoubtDialog = false;
    this.doubt = null;
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
