from sys import argv

FILENAME = "TournamentsData.csv"
NUM_ITERATIONS = int(argv[1])

print("Nigga " + argv[1])

open(FILENAME, "w").close()	# clean up file

file = open(FILENAME, "a")

for i in range(NUM_ITERATIONS):
	file.write("LeTournament" + str(i+1) + "\n")

file.close()
