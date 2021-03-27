package client;

import data.Data;
import exceptions.CommandNotFoundException;
import exceptions.RecursiveScript;
import processor.ConsoleProcessor;
import processor.FileProcessor;
import processor.Processor;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final static String path = System.getenv().get("LAB5");

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3345)) {
            ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
            //DataInputStream dis = new DataInputStream(socket.getInputStream());
            boolean stop = false;
            System.out.println("Client connected to server");
            if (path != null) {
                FileProcessor fileProcessor = null;
                try {
                    fileProcessor = new FileProcessor(path, null);
                    List<Data> tickets = fileProcessor.readDataFromCsv();
                    try {
                        for (Data d : tickets) {
                            ous.writeObject(d);
                            System.out.println(getAnswer(socket));
                        }
                    } catch (IOException e) {
                        System.out.println("Server is ill. Try to reconnect later");
                        stop = true;
                    }

                } catch (IOException e) {
                    System.out.println("Something wrong with file: " + path);
                } catch (RecursiveScript e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("File not found");
            }

            if (!stop) {
                ConsoleProcessor consoleProcessor = new ConsoleProcessor();
                while (true) {
                    try {
                        if (doCommands(consoleProcessor, ous, socket)) {
                            break;
                        }
                        //String ans = dis.readUTF();
                        //System.out.println(ans);
                    } catch (CommandNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

        } catch (SocketException e) {
            System.out.println("The server is tired. Try to reconnect later");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean doCommands(Processor processor, ObjectOutputStream ous, Socket socket) throws CommandNotFoundException {
        try {
            for (Data d : processor.readData()) {
                ous.writeObject(d);
                String ans = getAnswer(socket);
                if (ans.equals("exit")) {
                    return true;
                }
                System.out.println(ans);
            }
        } catch (IOException e) {
            System.out.println("Server is closed");
            return true;
        }
        return false;
    }

    public static List<Data> getCommands(String command, Processor processor) throws CommandNotFoundException, IOException {
        //Object[] args = new Object[2];
        List<Data> coms = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        switch (command) {
            case "help":
            case "info":
            case "show":
            case "clear":
            case "sum_of_discount":
            case "max_by_comment":
            case "print_unique_price":
            case "exit":
                coms.add(new Data(command, args));
                break;

            case "add":
            case "add_if_max":
            case "add_if_min":
            case "remove_greater":
                args.add(processor.getTicket());
                coms.add(new Data(command, args));
                break;

            case "update":
                args.add(processor.getId());
                args.add(processor.getTicket());
                coms.add(new Data(command, args));
                break;
            case "remove_by_id":
                args.add(processor.getId());
                coms.add(new Data(command, args));
                break;
            case "execute_script":
                try {
                    FileProcessor fileProcessor = new FileProcessor(processor.getName(), processor.getHistory());
                    coms.addAll(fileProcessor.readData());
                } catch (RecursiveScript e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                throw new CommandNotFoundException("Command \"" + command + "\" doesn't exist");
        }
        //System.out.println(coms);
        return coms;
    }

    private static String getAnswer(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder ans = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            ans.append((char) c);
        }
        return ans.toString();
    }
}
