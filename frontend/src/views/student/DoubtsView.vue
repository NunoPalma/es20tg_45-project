<template>
  <div class="container">
    <h2>Your Doubts</h2>
    <ul>
      <li class="list-header ">
        <div class="col">Question</div>
        <div class="col">Title</div>
        <div class="col">Status</div>
        <div class="col">Date</div>
        <div class="col last-col"></div>
      </li>
      <li
        class="list-row"
        v-for="doubt in doubts"
        :key="doubt.id"
        @click="seeDoubt(doubt)"
      >
        <div class="col">
          {{ doubt.questionTitle }}
        </div>
        <div class="col">
          {{ doubt.title }}
        </div>
        <div class="col">
          <v-chip text-color="white" v-if="!doubt.clarificationDto" color="red">{{ doubt.status }}</v-chip>
          <v-chip text-color="white" v-if="doubt.clarificationDto" color="green">{{ doubt.status }}</v-chip>
        </div>
        <div class="col">
          {{ doubt.creationDate }}
        </div>
        <div class="col last-col">
          <i class="fas fa-chevron-circle-right" />
        </div>
      </li>
    </ul>
    <see-doubt-dialog
      v-if="doubt"
      v-model="seeDoubtDialog"
      :doubt="doubt"
      v-on:see-doubt="onSeeDoubt"
      v-on:close-dialog="onCloseDialog"
    />
  </div>
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

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.doubts = await RemoteServices.getDoubts();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
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
