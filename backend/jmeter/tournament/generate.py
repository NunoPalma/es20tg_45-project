

FILENAME = "TournamentsData.csv"
NUM_ITERATIONS = 1000

open(FILENAME, "w").close()	# clean up file

file = open(FILENAME, "a")

for i in range(NUM_ITERATIONS):
	file.write("LeTournament" + str(i+1) + "\n")

file.close()
