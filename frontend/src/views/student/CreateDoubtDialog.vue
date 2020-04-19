<template>
  <v-dialog
    :value="dialog"
    @input="$emit('close-dialog')"
    @keydown.esc="$emit('close-dialog')"
    max-width="75%"
    max-height="80%"
  >

    <v-card>
      <v-card-title>
        <span class="headline">
          New Doubt
        </span>
      </v-card-title>

      <v-card-text class="text-left" v-if="isCreateDoubt">
        <v-container grid-list-md fluid>
          <v-layout column wrap>
            <v-flex xs24 sm12 md8>
              <v-text-field
                v-model="newDoubt.content"
                label="Content"
                data-cy="Content"
              />
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn
          color="blue darken-1"
          @click="$emit('close-dialog')"
          data-cy="cancelButton"
          >Cancel</v-btn
        >
        <v-btn color="blue darken-1" @click="saveDoubt" data-cy="saveButton"
          >Create</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import Doubt from '@/models/management/Doubt';
@Component
export default class CreateDoubtDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop({ type: Doubt, required: true }) readonly doubt!: Doubt;
  @Prop({ required: true }) readonly quizId!: number;
  newDoubt!: Doubt;
  quizQuestionId!: number;
  isCreateDoubt: boolean = true;
  created() {
    this.newDoubt = new Doubt(this.doubt);
    this.quizQuestionId = this.quizId;
  }
  async saveDoubt() {
    try {
      const result = await RemoteServices.createDoubt(
        this.newDoubt,
        this.quizQuestionId
      );
      this.$emit('new-doubt', result);
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
  }
}
</script>
