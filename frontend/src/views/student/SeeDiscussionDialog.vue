<template>
  <v-dialog
    :value="dialog"
    @input="$emit('close-dialog')"
    @keydown.esc="$emit('close-dialog')"
    max-width="75%"
    max-height="80%"
  >
    <v-simple-table>
      <template v-slot:default>
        <thead>
          <tr>
            <th class="text-left"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in discussion.postsDto" :key="item.name">
            <td>{{ item.content }}</td>
            <td v-if="item.clarificationDto">{{ item.clarificationDto.description }}</td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>

    <!-- Comment
    <v-container>
      <v-card>
        <v-card-title>
          <span class="headline">
            Discussions
          </span>
        </v-card-title>

        <v-card-title>
          <span class="headline">
            A sua discussão:
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
            A resposta de um docente:
          </span>
        </v-card-title>

        <v-card-text class="text-left" v-if="!Doubt.clarificationDto">
          <v-container grid-list-md fluid>
            <v-layout column wrap>
              <v-flex xs24 sm12 md8>
                <v-card> Ainda não existe uma resposta a sua duvida...</v-card>
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
            @click="$emit('close-dialog')"
            data-cy="cancelButton"
            >Back</v-btn
          >
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
    -->
  </v-dialog>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import Doubt from '@/models/management/Doubt';
import Discussion from '@/models/management/Discussion';
@Component
export default class EditDoubtDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop({ type: Discussion, required: true }) readonly discussion!: Discussion;
  Discussion!: Discussion;
  created() {
    console.log(this.discussion);
    this.Discussion = new Discussion(this.discussion);
  }
}
</script>
