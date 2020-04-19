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
          New Tournament
        </span>
			</v-card-title>

			<v-card-text class="text-left" v-if="editTournament">
				<v-container grid-list-md fluid>
					<v-layout column wrap>
						<v-flex xs24 sm12 md8>
							<p v-if="isCreateTournament"><b>Name:</b> {{ editTournament.name }} </p>
							<v-text-field
									v-if="!isCreateTournament"
									v-model="editTournament.name"
									label="Name"
									data-cy="Name"
							/>
						</v-flex>
						<v-menu>
							<template v-slot:activator="{ on }">
								<v-text-field :value="formattedDate(start_date)" label="Start date" prepend-icon="mdi-calendar-range" v-on="on"></v-text-field>
							</template>
							<v-datetime-picker v-model="start_date"></v-datetime-picker>
						</v-menu>
							<v-menu>
								<template v-slot:activator="{ on }">
									<v-text-field :value="formattedDate(end_date)" label="End date" prepend-icon="mdi-calendar-range" v-on="on"></v-text-field>
								</template>
								<v-datetime-picker v-model="end_date"></v-datetime-picker>
							</v-menu>
						<v-card class="table">
							<v-data-table
									:headers="headers"
									:items="topics"
									:search="search"
									disable-pagination
									:hide-default-footer="true"
									:mobile-breakpoint="0"
									multi-sort
							>
								<template v-slot:item.selected="{ item }">
									<v-checkbox v-model="selectedBOis[item.name]" @change="onCheckboxChange(item.name)" primary hide-details></v-checkbox>
								</template>
							</v-data-table>
						</v-card>
							<p class="pl-0">Number of Questions</p>
							<v-btn-toggle
									v-model="editTournament.numQuestions"
									mandatory
									class="button-group"
							>
								<v-btn text value="5">5</v-btn>
								<v-btn text value="10">10</v-btn>
								<v-btn text value="20">20</v-btn>
							</v-btn-toggle>
					</v-layout>
				</v-container>
			</v-card-text>

			<v-card-actions>
				<v-spacer/>
				<v-btn
						color="blue darken-1"
						@click="$emit('close-dialog')"
						data-cy="cancelButton"
				>Cancel
				</v-btn
				>
				<v-btn color="blue darken-1" @click="saveTournament" data-cy="saveButton"
				>Save
				</v-btn
				>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script lang="ts">
    import {Component, Model, Prop, Vue} from 'vue-property-decorator';
    import RemoteServices from '@/services/RemoteServices';
    import Course from '@/models/user/Course';
    import Tournament from '@/models/management/Tournament';
    import Topic from '@/models/management/Topic';
    import format from 'date-fns/format'

    @Component
    export default class EditTournamentDialog extends Vue {
        @Model('dialog', Boolean) dialog!: boolean;
        //@Prop({type: Course, required: true}) readonly tournament!: Tournament;	// type: Course wtf should be TOurnament maube?
        start_date: string = '';
        end_date: string = '';
        editTournament!: Tournament;
        isCreateTournament: boolean = false;
        topics: Topic[] = [];
        search: string = '';
        headers: object = [
            {text: 'Name', value: 'name', align: 'left', width: '30%'},
            {text: 'Selected', value: 'selected', align: 'center', sortable: false, width: '20%'}
        ];
        selectedBOis: {[name: string]: boolean } = {};

        async created() {
            this.editTournament = new Tournament();
            this.isCreateTournament = !!this.editTournament.name;

			try {
			  this.topics = await RemoteServices.getTopics();
			  for (let i = 0; i < this.topics.length; ++i)
			      this.selectedBOis[this.topics[i].name] = false;
			} catch (error) {
			  await this.$store.dispatch('error', error);
			}

			await this.$store.dispatch('loading');
        }

		formattedDate(date: Date) {
        	return date ? format(date, 'Pp') : '';
		}

		onCheckboxChange(name: string) {
            this.selectedBOis[name] = !this.selectedBOis[name];
            console.log('now it\'s ' + this.selectedBOis[name]);
		}

        async saveTournament() {
            if (this.editTournament) {
                this.editTournament.startDate = this.start_date;
                this.editTournament.endDate = this.end_date;
                for (let i = 0; i < this.topics.length; ++i) {
                    if (this.selectedBOis[this.topics[i].name]) {
                        console.log('mein nigger');
                        this.editTournament.topics.push(this.topics[i]);
					}
				}

                this.editTournament.startDate = this.start_date;
                this.editTournament.endDate = this.end_date;
			}

            if (this.editTournament &&
				(!this.editTournament.startDate ||
				!this.editTournament.endDate ||
				!this.editTournament.topics ||
				!this.editTournament.numQuestions)) {
                await this.$store.dispatch(
                    'error',
                    'Course must have name, acronym and academicTerm'
                );
                return;
			}

            if (this.editTournament) {
                try {
                    const result = await RemoteServices.createTournament(this.editTournament);
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');
                    console.log('crl crl crl crl');


                    this.$emit('new-tournament', result);
                } catch (error) {
                    await this.$store.dispatch('error meu preto', error);
                }
			}
        }
    }
</script>