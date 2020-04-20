<template>
  <v-dialog
    :value="dialog"
    @input="$emit('close-dialog')"
    @keydown.esc="$emit('close-dialog')"
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
            A sua Duvida:
          </span>
        </v-card-title>

        <v-card-text class="text-left">
          <v-container grid-list-md fluid>
            <v-layout column wrap>
              <v-flex xs24 sm12 md8>
                <v-card-subtitle>{{Doubt.content}}</v-card-subtitle>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-card-title>
          <span class="headline">
            A resposta de um docente:
          </span>
        </v-card-title>

        <v-card-text class="text-left" v-if="!Doubt.clarificationDto">
          <v-container grid-list-md fluid>
            <v-layout column wrap>
              <v-flex xs24 sm12 md8>
                <v-card-subtitle> Ainda não existe uma resposta a sua duvida...</v-card-subtitle>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-card-text class="text-left" v-if="Doubt.clarificationDto">
          <v-container grid-list-md fluid>
            <v-layout column wrap>
              <v-flex xs24 sm12 md8>
                <v-card-subtitle>{{Doubt.clarificationDto.description}}</v-card-subtitle>
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
export default class EditDoubtDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop({ type: Doubt, required: true }) readonly doubt!: Doubt;
  Doubt!: Doubt;
  created() {
    console.log(this.doubt);
    this.Doubt = new Doubt(this.doubt);
  }
}
</script>
