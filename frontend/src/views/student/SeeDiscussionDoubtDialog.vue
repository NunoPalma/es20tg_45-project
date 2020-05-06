<template>
  <v-dialog
    :value="dialog"
    @input="$emit('close-question-dialog')"
    @keydown.esc="$emit('close-question-dialog')"
    max-width="75%"
    max-height="80%"
  >
    <v-container>
      <v-card>
        <v-card-title>
          <span class="headline">
            Discussions
          </span>
        </v-card-title>

        <v-card-title>
          <span class="headline">
            A Duvida de {{Doubt.author}}
          </span>
        </v-card-title>

        <v-card-text class="text-left">
          <v-container grid-list-md fluid>
            <v-layout column wrap>
              <v-flex xs24 sm12 md8>
                <v-card>{{ Doubt.content }}</v-card>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-card-title>
          <span class="headline">
            A resposta do docente:</span>
        </v-card-title>

        <v-card-text class="text-left" v-if="!Doubt.clarificationDto">
          <v-container grid-list-md fluid>
            <v-layout column wrap>
              <v-flex xs24 sm12 md8>
                <v-card> Ainda não há resposta para esta duvida...</v-card>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-card-text class="text-left" v-if="Doubt.clarificationDto">
          <v-container grid-list-md fluid>
            <v-layout column wrap>
              <v-flex xs24 sm12 md8>
                <v-card>{{ Doubt.clarificationDto.description }}</v-card>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer />
          <v-btn
            color="blue darken-1"
            @click="$emit('close-question-dialog')"
            data-cy="cancelButton"
            >Back</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-container>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import Doubt from '@/models/management/Doubt';
@Component
export default class SeeQuestionDoubtDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop({ type: Doubt, required: true }) readonly doubt!: Doubt;
  Doubt!: Doubt;
  created() {
    console.log(this.doubt);
    this.Doubt = new Doubt(this.doubt);
  }
}
</script>
