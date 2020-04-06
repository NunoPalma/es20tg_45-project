<template>
	<v-card class="table">
		<v-data-table
				:headers="headers"
				:items="tournaments"
				:search="search"
				disable-pagination
				:hide-default-footer="true"
				:mobile-breakpoint="0"
				multi-sort
		>
			<template v-slot:top>

				<v-card-title>
					<v-text-field
							v-model="search"
							append-icon="search"
							label="Search"
							class="mx-2"
					/>
					<v-spacer />
					<v-btn color="primary" dark @click="newTournament" data-cy="createButton"
					>New Tournament</v-btn
					>
				</v-card-title>
			</template>

			<template v-slot:item.action="{ item }">
				<v-tooltip bottom>
					<template v-slot:activator="{ on }">
						<v-icon
								small
								class="mr-2"
								v-on="on"
								@click="createFromCourse(item)"
								data-cy="createFromCourse"
						>cached</v-icon
						>
					</template>
					<span>Create from Course</span>
				</v-tooltip>
				<v-tooltip bottom>
					<template v-slot:activator="{ on }">
						<v-icon
								small
								class="mr-2"
								v-on="on"
								@click="deleteCourse(item)"
								color="red"
								data-cy="deleteCourse"
						>delete</v-icon
						>
					</template>
					<span>Delete Course</span>
				</v-tooltip>
			</template>

			<template v-slot:item.name="{item}">
				<v-layout align="left">
					{{item.name}}
				</v-layout>
			</template>

			<template v-slot:item.status="{item}">
				<v-layout align="left">
					{{item.state}}
				</v-layout>
			</template>

			<template v-slot:item.creator="{item}">
				<v-layout align="left">
						{{item.creator.name}}
				</v-layout>
			</template>

			<template v-slot:item.startDate="{item}">
				<v-layout align="left">
						{{item.startDate}}
				</v-layout>
			</template>

			<template v-slot:item.endDate="{item}">
				<v-layout align="left">
						{{ item.endDate }}
				</v-layout>
			</template>

		</v-data-table>

		<edit-tournament-dialog
				v-if="currentCourse"
				v-model="editTournamentDialog"
				:course="currentCourse"
				v-on:new-course="onCreateTournament"
				v-on:close-dialog="onCloseDialog"
		/>
	</v-card>
</template>

<script lang="ts">

	import { Component, Vue } from 'vue-property-decorator';
	import Course from '@/models/user/Course';
	import Tournament from '@/models/management/Tournament';
	import RemoteServices from '@/services/RemoteServices';
	import EditTournamentDialog from '@/views/student/EditTournamentDialog.vue';
	@Component({
		components: {
			'edit-tournament-dialog': EditTournamentDialog
		}
	})
	export default class TournamentsView extends Vue {
		tournaments: Tournament[] = [];
		currentCourse: Course | null = null;
		editTournamentDialog: boolean = false;
		search: string = '';
		headers: object = [
			{
				text: 'Creator',
				value: 'creator',
				align: 'left',
				width: '10%'
			},
			{ text: 'Name', value: 'name', align: 'left', width: '10%' },
			{
				text: 'Start Date',
				value: 'startDate',
				align: 'left',
				width: '10%'
			},
			{
				text: 'Status',
				value: 'status',
				align: 'left',
				width: '10%'
			},
			{
				text: 'Actions',
				value: 'action',
				align: 'center',
				sortable: false,
				width: '10%'
			}
		];
		async created() {
			await this.$store.dispatch('loading');
			try {
				this.tournaments = await RemoteServices.getAvailableTournaments();
			} catch (error) {
				await this.$store.dispatch('error', error);
			}
			await this.$store.dispatch('clearLoading');
		}
		newTournament() {
			this.currentCourse = new Course();
			this.editTournamentDialog = true;
		}
		/*
=======
            {
                text: 'Status',
                value: 'status',
                align: 'left',
                width: '10%'
            },
            {
                text: 'Actions',
                value: 'action',
                align: 'center',
                sortable: false,
                width: '10%'
            }
        ];
        async created() {
            await this.$store.dispatch('loading');
            try {
            	this.tournaments = await RemoteServices.getAvailableTournaments();
            } catch (error) {
                await this.$store.dispatch('error', error);
            }
            await this.$store.dispatch('clearLoading');
        }
        newTournament() {
            this.currentCourse = new Course();
            this.editTournamentDialog = true;
        }
        /*
>>>>>>> Stashed changes
        createFromCourse(course: Course) {
            this.currentCourse = new Course(course);
            this.currentCourse.courseExecutionId = undefined;
            this.currentCourse.courseExecutionType = 'EXTERNAL';
            this.currentCourse.acronym = undefined;
            this.currentCourse.academicTerm = undefined;
            this.editCourseDialog = true;
        }
        */
		async onCreateTournament(tournament: Tournament) {
			this.tournaments.unshift(tournament);
			this.editTournamentDialog = false;
			this.currentCourse = null;
		}
		onCloseDialog() {
			this.editTournamentDialog = false;
			this.currentCourse = null;
		}
		/*
        async deleteCourse(courseToDelete: Course) {
            if (confirm('Are you sure you want to delete this question?')) {
                try {
                    await RemoteServices.deleteCourse(courseToDelete.courseExecutionId);
                    this.courses = this.courses.filter(
                        course => course.courseExecutionId != courseToDelete.courseExecutionId
                    );
                } catch (error) {
                    await this.$store.dispatch('error', error);
                }
            }
        }
         */
	}
</script>

<style lang="scss" scoped></style>
