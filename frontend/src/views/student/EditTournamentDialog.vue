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
						<v-flex xs24 sm12 md8>
							<p v-if="isCreateTournament"><b>Start date:</b> {{ editTournament.startDate }} </p>
							<v-text-field
									v-if="!isCreateTournament"
									v-model="editTournament.name"
									label="Start date (YYYY-MM-dd HH:mm:ss)"
									data-cy="Name"
							/>
						</v-flex>
						<v-flex xs24 sm12 md8>
							<p v-if="isCreateTournament"><b>End date:</b> {{ editTournament.endDate }} </p>
							<v-text-field
									v-if="!isCreateTournament"
									v-model="editTournament.name"
									label="End date (YYYY-MM-dd HH:mm:ss)"
									data-cy="Name"
							/>
						</v-flex>
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
								<template v-slot:item.action="{ item }">
									<v-tooltip bottom>
										<template v-slot:activator="{ on }">
											<v-icon
													small
													class="mr-2"
													v-on="on"
													@click="createFromCourse(item)"
													data-cy="createFromCourse"
											>cached
											</v-icon
											>
										</template>
										<span>Create from Course</span>
									</v-tooltip>
								</template>
							</v-data-table>
						</v-card>
							<p class="pl-0">Number of Questions</p>
							<v-btn-toggle
									v-model="statementManager.numberOfQuestions"
									mandatory
									class="button-group"
							>
								<v-btn text value="5">5</v-btn>
								<v-btn text value="10">10</v-btn>
								<v-btn text value="20">20</v-btn>
							</v-btn-toggle>

						<!--
						<v-flex xs24 sm12 md8>
							<p>
								<b>Course Execution Type:</b> Merda
								{{ editCourse.courseExecutionType }}
							</p>
						</v-flex>
						<v-flex xs24 sm12 md8>
							<v-text-field
									v-model="editCourse.acronym"
									label="Acronym"
									data-cy="Acronym"
							/>
						</v-flex>
						<v-flex xs24 sm12 md8>
							<v-text-field
									v-model="editCourse.academicTerm"
									label="Academic Term"
									data-cy="AcademicTerm"
							/>
						</v-flex>
						-->
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
    import Tournament from "@/models/management/Tournament";
    import Topic from "@/models/management/Topic";
    import StatementManager from "@/models/statement/StatementManager";

    @Component
    export default class EditTournamentDialog extends Vue {
        @Model('dialog', Boolean) dialog!: boolean;
        @Prop({type: Course, required: true}) readonly tournament!: Tournament;
        editTournament!: Tournament;
        isCreateTournament: boolean = false;
        topics: Topic[] = [];
        search: string = '';
        headers: object = [
            {text: 'Name', value: 'name', align: 'left', width: '30%'},
            {text: 'Selected', value: 'selected', align: 'center', sortable: false, width: '20%'}
        ];
        statementManager: StatementManager = StatementManager.getInstance;

        created() {
            this.editTournament = new Tournament(this.tournament);
            this.isCreateTournament = !!this.editTournament.name;
        }

        /*
        async saveTournament() {
            if (
                this.editCourse &&
                (!this.editCourse.name ||
                    !this.editCourse.acronym ||
                    !this.editCourse.academicTerm)
            ) {
                await this.$store.dispatch(
                    'error',
                    'Course must have name, acronym and academicTerm'
                );
                return;
            }
            if (this.editCourse && this.editCourse.courseExecutionId == null) {
                try {
                    //const result = await RemoteServices.createCourse(this.editCourse);
                    //this.$emit('new-course', result);
                } catch (error) {
                    await this.$store.dispatch('error', error);
                }
            }
        }

         */
    }
</script>