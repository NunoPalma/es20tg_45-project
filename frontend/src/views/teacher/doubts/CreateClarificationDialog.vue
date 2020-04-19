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
        <span v-if="creating" class="headline"> Resolver dúvida </span>
        <span v-if="!creating" class="headline"> Detalhes da dúvida </span>
      </v-card-title>

      <v-card-text class="text-left" v-if="newClarification">
        <v-container grid-list-md fluid>
          <v-layout column wrap>
            <v-flex xs24 sm12 md8>
              <p><b>Autor:</b> {{ doubt.author }}</p>
            </v-flex>
            <v-flex xs24 sm12 md8>
              <p><b>Dúvida:</b> {{ doubt.content }}</p>
            </v-flex>
            <v-flex xs24 sm12 md8 v-if="!creating">
              <p><b>Resposta:</b> {{ doubt.clarificationDto.description }}</p>
            </v-flex>
            <v-flex xs24 sm12 md8>
              <v-text-field
                v-if="creating"
                v-model="newClarification.description"
                label="Responder..."
                data-cy="Response"
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
        <v-btn
          v-if="creating"
          color="blue darken-1"
          data-cy="saveButton"
          @click="saveClarification"
          >Reply</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue } from 'vue-property-decorator';
import Doubt from '../../../models/management/Doubt';
import Clarification from '../../../models/management/Clarification';
import RemoteServices from '@/services/RemoteServices';
@Component
export default class CreateClarificationDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop({ type: Doubt, required: true }) readonly doubt!: Doubt;
  @Prop({ type: Boolean, required: true }) readonly creating!: boolean;
  newClarification!: Clarification;

  created() {
    this.newClarification = new Clarification();
    this.newClarification.author = this.doubt.author;
  }

  async saveClarification() {
    if (
      this.newClarification &&
      (!this.newClarification.description || !this.newClarification.author)
    ) {
      await this.$store.dispatch('error', 'Clarification must have a text');
      return;
    }

    if (
      this.newClarification &&
      this.newClarification.description &&
      this.newClarification.author &&
      this.doubt.id != null
    ) {
      try {
        await RemoteServices.createClarification(
          this.doubt.id,
          this.newClarification
        );
        this.$emit('new-clarification');
      } catch (error) {
        await this.$store.dispatch('error', error);
      }
    }
  }
}
</script>
