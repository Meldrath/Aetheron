package com.aetheron;

import java.util.*;
import java.util.stream.Collectors;

public class Processor implements IProcessor {

    private static final Set<String> cardinalDirections = new HashSet<>(Arrays.asList("east", "west", "north", "south", "up", "down"));
    private static final Map<String, String> cardinalDirectionAbbreviations = cardinalDirections.stream().collect(Collectors.toMap(d -> d.substring(0, 1), d -> d));

    public void process(String input, Body body, Room room, IArea area) {
        if (input == null || "".equals(input)) {
            return;
        }

        if (input.startsWith("say ")) {
            room.getLocalBodies().forEach(otherBody -> {
                if (otherBody.equals(body)) {
                    otherBody.sendOutput("You say, \"" + input + "\"\n");
                }
                else {
                    otherBody.sendOutput(body.stringProps.get("name") + " says, \"" + input + "\"\n");
                }
            });
        }
        else if (input.equals("look")) {
            room.displayTo(body);
        }
        else if ("inventory".startsWith(input)) {
            if (body.items.isEmpty()) {
                body.sendOutput("You have nothing.\n");
            }
            else {
                body.items.forEach(thing -> body.sendOutput(thing.getName() + "\n"));
            }
        }
        else if (input.startsWith("get") || input.startsWith("take")) {
            final String[] words = input.split("\\s+", 2);
            if (words.length == 1){
                body.sendOutput(words[0] + " what?\n");
            }
            else {
                final Optional<Item> opt = room.getItem(words[1]);
                opt.ifPresent(item -> {
                    room.remove(item);
                    body.add(item);
                    body.sendOutput("You got the " + item.getName() + ".\n");
                });
                if (!opt.isPresent()) {
                    body.sendOutput("You don't see a " + words[0] + " here.\n");
                }
            }
        }
        else if (input.startsWith("drop") || input.startsWith("discard")) {
            final String[] words = input.split("\\s+", 2);
            if (words.length == 1){
                body.sendOutput(words[0] + " what?\n");
            }
            else {
                final Optional<Item> opt = body.getItem(words[1]);
                opt.ifPresent(item -> {
                    body.remove(item);
                    room.add(item);
                    body.sendOutput("You dropped the " + item.getName() + ".\n");
                });
                if (!opt.isPresent()) {
                    body.sendOutput("You don't have a " + words[0] + ".\n");
                }
            }
        }
        else if (input.startsWith("create")) {
            final String[] words = input.split("\\s+");
            if (words.length < 2) {
                body.sendOutput("Create what?\n");
            }
            else if (words[1].equals("room")) {
                if (words.length == 4) {
                    final String dir = words[2];
                    final String backDir = words[3];
                    final Room newRoom = new Room("a new room");
                    area.add(newRoom);
                    room.exits.put(dir, new Exit(dir, newRoom));
                    newRoom.exits.put(backDir, new Exit(backDir, room));
                    body.sendOutput("Created room to the " + dir + ".\n");
                }
                else {
                    body.sendOutput("Syntax: create room <direction> <back direction>\n");
                }
            }
            else if (words[1].equals("object")) {
                final String[] args = input.substring("create object ".length()).trim().split("' '");
                if (args.length == 2 && args[0].length() > 1 && args[0].startsWith("'") && args[1].length() > 1 && args[1].endsWith("'")) {
                    final String name = args[0].substring(1);
                    final String description = args[1].substring(1);
                    room.add(new Item(name, description));
                    body.sendOutput("Created object.\n");
                }
                else {
                    body.sendOutput("Syntax: create object '<name>' '<description>'\n");
                }
            }
            else {
                body.sendOutput("Create what?\n");
            }
        }
        else if (input.startsWith("go ") && input.length() > 3) {
            final String dir = input.substring(3);
            goDir(body, dir, room);
        }
        else if (cardinalDirections.contains(input)) {
            goDir(body, input, room);
        }
        else if (cardinalDirectionAbbreviations.containsKey(input)) {
            final String dir = cardinalDirectionAbbreviations.get(input);
            goDir(body, dir, room);
        }
        else {
            body.sendOutput("I don't understand, \"" + input + "\"\n");
        }
    }

    private void goDir(Body body, String dir, Room room) {
        if (room.exits.containsKey(dir)) {
            room.moveTo(body, room.exits.get(dir).destination);
        } else {
            body.sendOutput("You cannot go that way.\n");
        }
    }
}
