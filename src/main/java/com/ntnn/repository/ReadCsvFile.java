package com.ntnn.repository;

import com.ntnn.model.User;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReadCsvFile {
    private static final ReadCsvFile INSTANCE = new ReadCsvFile();

    public static ReadCsvFile getInstance() {
        return INSTANCE;
    }

    public List<User> readFromFile(String pathFile) throws FileNotFoundException {
        List<User> lst = new CsvToBeanBuilder(new FileReader(pathFile))
                .withType(User.class)
                .build()
                .parse();
        return lst;
    }

    public void saveErrors(List<User> lstErrors, String path) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeAll(createLst(lstErrors));
        }
    }

    public void saveFileJson(JSONArray jsonArray, String path) throws IOException {
        File myObj = new File(path + "output.json");
        if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
            try (FileWriter fileWriter = new FileWriter(path + "output.json")) {
                fileWriter.write(jsonArray.toJSONString());
            }
        } else {
            try (FileWriter fileWriter = new FileWriter(path + "output.json")) {
                fileWriter.write(jsonArray.toJSONString());
            }
        }
    }

    public List<String[]> createLst(List<User> lstErrors) {
        List<String[]> lst = new ArrayList<>();
        String[] headers = {"TITLE", "FIRST_NAME", "LAST_NAME", "EMAIL"};
        lst.add(headers);
        lst.addAll(lstErrors.stream().map(v -> {
            String[] strArr = {v.getTitle(), v.getFirstName(), v.getLastName(), v.getEmail()};
            return strArr;
        }).collect(Collectors.toList()));
        return lst;
    }
}
