# CsvPrefixSearch
## Implementation of prefix search in a CSV File

### Usage
When starting the program, the following parameters are specified:
--data example.csv — the path to the csv file,
--indexed-column-id 3 — column for which the search is performed,
--input-file input-path-to-file.txt — the path to the file with the input search strings, one string per line
--output-file output-path-to-file.json is the path to the search results file. If there is no file,
it should be created, if there is, overwritten. The file format is json, containing
the following fields:
1. initTime is a number, the time in milliseconds of initialization from the start of the program to
the readiness to perform the first search. It can include, among other things, the proofreading
of the --input-file.
2. result — an array, each element of which is the result of a search on the line
of the --input-file file. Array objects have the following fields: 
  a) search — the search word;
  b) result — an array of line numbers suitable for the search, sorted by column. The sorting is lexicographic for string columns, numeric
  for numeric columns. Line number is the first column;
  c) time is the number in milliseconds spent on performing a string search.

Example of the output file contents:
{“initTime”:100, “result”:[
  {“search”: ”Bower”, “result”: [1, 2], “time”: 10},
  {“search”: ”Asa”, “result”: [8, 4], “time”: 10}
]}

