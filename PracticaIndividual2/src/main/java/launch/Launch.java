package launch;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import match.Partido;
import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import json.Json;

public class Launch
{
    public static void main(String[] args)
    {
        File myFile;
        FileWriter fileWriter;
        PrintWriter printWriter;
        BufferedWriter bufferedWriter;
        String fileContent;
        Partido partido = null;
        String nombreEntrenador= " ";
        List<String> entrenadores = new ArrayList<String>();

        try {
            myFile = new File("partidos.txt");
            fileWriter = new FileWriter(myFile);
            printWriter = new PrintWriter(myFile);
            printWriter.write("Los equipos que jugaron la final de la Eurocopa 2020 son: ");
            fileContent = FileUtils.readFileToString(new File("43.json"), "UTF-8");
            JsonNode rootJsonNode = Json.mapper().readTree(fileContent);
            if (rootJsonNode.isArray()) {
                JsonNode rootArrayJsonNode = (ArrayNode) rootJsonNode;
                final Iterator<JsonNode> iterator = rootArrayJsonNode.elements();
                while (iterator.hasNext())
                {
                    final JsonNode equiposJsonNode = iterator.next();

                    if (equiposJsonNode.has("home_team")) {
                        final JsonNode homeTeamNode = equiposJsonNode.get("home_team").get("home_team_name");

                        if (equiposJsonNode.has("competition_stage")) {
                            final JsonNode comepetitionStageNode = equiposJsonNode.get("competition_stage").get("name");
                            if (comepetitionStageNode.asText().equals("Final")) {
                                printWriter.write(homeTeamNode.asText() + " - ");
                            }
                        }
                    }

                    if (equiposJsonNode.has("away_team")) {
                        final JsonNode awayTeamNode = equiposJsonNode.get("away_team").get("away_team_name");

                        if (equiposJsonNode.has("competition_stage")) {
                            final JsonNode comepetitionStageNode = equiposJsonNode.get("competition_stage").get("name");
                            if (comepetitionStageNode.asText().equals("Final")) {
                                printWriter.write(awayTeamNode.asText()+"\n");
                            }
                        }
                    }
                }
                printWriter.write("Los entrenadores cuya nacionalidad no corresponde con la del pais que representa son: \n");

                final Iterator<JsonNode> iterator2 = rootArrayJsonNode.elements();
                while (iterator2.hasNext())
                {
                    final JsonNode equiposJsonNode = iterator2.next();
                    if (equiposJsonNode.has("home_team"))
                    {
                        final JsonNode homeTeamJsonNode = equiposJsonNode.get("home_team");
                        if (homeTeamJsonNode.has("country"))
                        {
                            final JsonNode homeTeamCountryJsonNode = homeTeamJsonNode.get("country");
                            if (homeTeamCountryJsonNode.has("name"))
                            {
                                final JsonNode homeTeamCountryNameJsonNode = homeTeamCountryJsonNode.get("name");

                                String nombrePais = homeTeamCountryNameJsonNode.asText();

                                if (homeTeamJsonNode.has("managers"))
                                {
                                    JsonNode managersJsonNode = homeTeamJsonNode.get("managers");
                                    if (managersJsonNode.isArray())
                                    {
                                        JsonNode managersArrayJsonNode = (ArrayNode) managersJsonNode;
                                        final Iterator<JsonNode> iterator3 = managersArrayJsonNode.elements();
                                        while (iterator3.hasNext())
                                        {
                                            JsonNode managerJsonNode = iterator3.next();
                                            if (managerJsonNode.has("name"))
                                            {
                                                final JsonNode homeTeamManagerName = managerJsonNode.get("name");
                                                nombreEntrenador = homeTeamManagerName.asText();
                                            }
                                            if (managerJsonNode.has("country"))
                                            {
                                                final JsonNode homeTeamManagerCountry = managerJsonNode.get("country");
                                                if (homeTeamManagerCountry.has("name"))
                                                {
                                                    final JsonNode homeTeamManagerCountryName = homeTeamManagerCountry.get("name");
                                                    String paisEntreandor = homeTeamManagerCountryName.asText();
                                                    if (!nombrePais.equals(paisEntreandor))
                                                    {
                                                        entrenadores.add(nombreEntrenador);
                                                        for(int i = 0; i< entrenadores.size(); i++)
                                                        {
                                                            if(entrenadores[i] )
                                                            {
                                                                printWriter.write(nombreEntrenador+ "\n");

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                final Iterator<JsonNode> iterator4 = rootArrayJsonNode.elements();
                printWriter.write("Partidos que se jugaron despues del 1 de julio de 2021: \n");
                while (iterator4.hasNext())
                {
                    final JsonNode equiposJsonNode = iterator4.next();
                    if (equiposJsonNode.has("home_team")) {
                        JsonNode homeTeamName = equiposJsonNode.get("home_team").get("home_team_name");
                        if (equiposJsonNode.has("match_date")) {
                            JsonNode matchDateNode = equiposJsonNode.get("match_date");
                            Date fechaEnunciado = new Date(2021 / 06 / 01);
                            Date dateMatch = new SimpleDateFormat("yyy-MM-dd").parse(matchDateNode.asText());
                            if (dateMatch.after(fechaEnunciado)) {
                                printWriter.write(homeTeamName.asText()+" - ");
                            }
                        }
                    }

                    if (equiposJsonNode.has("away_team")) {
                        JsonNode awayTeamName = equiposJsonNode.get("away_team").get("away_team_name");
                        if (equiposJsonNode.has("match_date")) {
                            JsonNode matchDateNode = equiposJsonNode.get("match_date");
                            Date fechaEnunciado = new Date(2021 / 06 / 01);
                            Date dateMatch = new SimpleDateFormat("yyy-MM-dd").parse(matchDateNode.asText());
                            if (dateMatch.after(fechaEnunciado)) {
                                printWriter.write(awayTeamName.asText()+"\n");
                            }
                        }
                    }
                }
            }





            fileWriter.flush();
            printWriter.flush();
            fileWriter.close();
            printWriter.close();
        }
        catch ( ParseException parseException ) {
            parseException.printStackTrace();
        }

        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }

    }

}
