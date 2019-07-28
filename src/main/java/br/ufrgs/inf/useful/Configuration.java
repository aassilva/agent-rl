package br.ufrgs.inf.useful;

import br.ufrgs.inf.agent.Action;
import br.ufrgs.inf.beans.State;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    public static double selfWeight = 1;
    public static double timeWeight = .1;
    public static double resourceShare = 0;
    public static int numberOfAgents = 50;
    public static int numberOfTrials = 100;
    public static double costOfLiving = 100;
    public static String stateActionSpace = "{{{100}:0,{0,300}:0}}";
    public static double temperature = 20;
    public static String runType = "";

    public static void setConfig(String[] args) throws IOException {

        List<String> lines = null;
        Path config = Paths.get("config.txt");
        lines = Files.readAllLines(config);

        for (String s : lines) {

            String[] split = s.split("=");
            String varName = split[0];

            if (split.length > 1) {

                String varValue = split[1];

                if (varName.equals("selfWeight")) selfWeight = Double.parseDouble(varValue);
                else if (varName.equals("timeWeight")) timeWeight = Double.parseDouble(varValue);
                else if (varName.equals("resourceShare")) resourceShare = Double.parseDouble(varValue);
                else if (varName.equals("numberOfAgents")) numberOfAgents = Integer.parseInt(varValue);
                else if (varName.equals("numberOfTrials")) numberOfTrials = Integer.parseInt(varValue);
                else if (varName.equals("costOfLiving")) costOfLiving = Double.parseDouble(varValue);
                else if (varName.equals("stateActionSpace")) stateActionSpace = varValue;
                else System.out.println("Parameter " + varName + " is not valid.");
            }
        }

        for (String s : args) {

            String[] split = s.split("=");
            String varName = split[0];

            if (split.length > 1) {

                String varValue = split[1];

                if (varName.equals("selfWeight")) selfWeight = Double.parseDouble(varValue);
                else if (varName.equals("timeWeight")) timeWeight = Double.parseDouble(varValue);
                else if (varName.equals("resourceShare")) resourceShare = Double.parseDouble(varValue);
                else if (varName.equals("numberOfAgents")) numberOfAgents = Integer.parseInt(varValue);
                else if (varName.equals("numberOfTrials")) numberOfTrials = Integer.parseInt(varValue);
                else if (varName.equals("costOfLiving")) costOfLiving = Double.parseDouble(varValue);
                else if (varName.equals("stateActionSpace")) stateActionSpace = varValue;
                else if (varName.equals("runType")) runType = varValue;
                else System.out.println("Parameter " + varName + " is not valid.");
            }
        }
    }

    public static int[] parseSASpace() throws Exception {

        List<String> tokens = new ArrayList<String>();
        List<Character> numbers = new ArrayList<Character>();

        for (char e : "0123456789".toCharArray()) {

            numbers.add(e);
        }
        String token = "";

        for (char c : stateActionSpace.toCharArray()) {

            if (numbers.contains(c) || (("" + c).equals(":") && (token.equals("")))) {
                token += c;
            } else if (("" + c).equals("{") || ("" + c).equals("}")) {
                if (token.equals("")) {
                    token += c;
                    tokens.add(token);
                    token = "";
                } else {
                    tokens.add(token);
                    token = "" + c;
                    tokens.add(token);
                    token = "";
                }
            } else {

                if (token.equals("")){}
                else {
                    tokens.add(token);
                    token = "";
                }
            }
        }

        if (!token.equals(""))
        {
            tokens.add(token);
            token = "";
        }

        int level = 0;
        int bottom = 0;
        int top = 0;
        List<Integer> r = new ArrayList<Integer>();
        List<String> nextStateTokens = new ArrayList<String>();

        for (String t : tokens) {

            if (t.equals("{")) {
                level++;
                if (level > 3) throw new Exception("S-A space level too deep."
                        + "Please make sure that stateActionSpace=" + stateActionSpace
                        + " is valid.");
            } else if (t.equals("}")) {
                level--;
                if (level == 2) {
                            Action a = new Action(r);
                    r = new ArrayList<Integer>();
                    top++;
                } else if (level == 1) {
                    ArrayList<Action> actions = new ArrayList<Action>();
                    actions.addAll(Action.getAllActions().subList(bottom, top));
                    bottom = top;
                            State s = new State(actions);
                } else if (level < 0) throw new Exception("S-A space levels unbalanced"
                        + "(extra '}'). Please make sure that stateActionSpace="
                        + stateActionSpace + " is valid.");
            } else if (t.startsWith(":")) nextStateTokens.add(t);
            else {
                if (level == 3) r.add(Integer.parseInt(t));
                else throw new Exception("Number found in improper level of {}. Please"
                        + "make sure that all numbers are in the third level of {}");
            }
        }

        if (level > 0) throw new Exception("S-A space levels unbalanced"
                + "(not enough '}'). Please make sure that stateActionSpace="
                + stateActionSpace + " is valid.");

        if (nextStateTokens.size() != Action.getAllActions().size()) throw new Exception(
                "The number of Action->State links does not match the total number of Actions."
                        + " Please make sure that there is exactly one \"':' + index\" for"
                        + " each Action in parameter stateActionSpace");

        for (int index = 0; index < nextStateTokens.size(); index++) {

            int stateNum = Integer.parseInt(nextStateTokens.get(index).substring(1));
            Action.getAllActions().get(index).setNextState(State.getAllStates().get(stateNum));
        }

        List<Integer> dimensions = new ArrayList<Integer>();

        for (State s : State.getAllStates()) {

            dimensions.add(s.getActions().size());
        }

        int[] d = new int[dimensions.size()];

        for (int i = 0; i < dimensions.size(); i++) {

            d[i] = dimensions.get(i);
        }

        return d;
    }

    public static String parameters() {

        return "[selfWeight=" + selfWeight + ", timeWeight=" + timeWeight + ", resourceShare="
                + resourceShare + ", numberOfAgents=" + numberOfAgents + ", numberOfTrials="
                + numberOfTrials + ", costOfLiving="
                + costOfLiving+ ", stateActionSpace=" + stateActionSpace + "]";

    }

}