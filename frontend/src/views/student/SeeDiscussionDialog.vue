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
            <td v-if="item.clarificationDto">
              {{ item.clarificationDto.description }}
            </td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>
    <template>
      <v-card>
        <v-card-text class="text-left" v-if="canCreateNewDoubt">
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
          <v-btn v-if="canCreateNewDoubt" color="blue darken-1" @click="addDoubt" data-cy="saveButton"
            >Create</v-btn
          >
        </v-card-actions>
      </v-card>
    </template>
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
  @Prop({ type: Number, required: true }) readonly id!: number;

  Discussion!: Discussion;
  newDoubt!: Doubt;
  canCreateNewDoubt: boolean = true;
  created() {
    console.log(this.discussion);
    console.log('nibba');
    this.newDoubt = new Doubt();
    this.Discussion = new Discussion(this.discussion);
    this.canCreateNewDoubt =
      this.discussion.postsDto[this.discussion.postsDto.length - 1]
        .clarificationDto != null;
    console.log(this.canCreateNewDoubt);
  }

  async addDoubt() {
    if (this.newDoubt && !this.newDoubt.content) {
      await this.$store.dispatch('error', 'Doubt must have Content');
      return;
    } else {
      this.newDoubt.creationDate = new Date(Date.now()).toLocaleString();
      console.log(this.newDoubt.creationDate);
      this.newDoubt.isNew = true;
      this.discussion.postsDto.unshift(this.newDoubt);
      try {
        const result = await RemoteServices.addDoubt(this.id, this.newDoubt);
        this.$emit('new-discussion', result);
      } catch (error) {
        await this.$store.dispatch('error', error);
      }
    }
  }
}
</script>
