package chat;

import chat.model.ChatRoom;
import chat.model.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Implementa la interfaz gráfica basada en línea de comandos de la aplicación. Esta clase no
 * debe ser modificada.
 */
public class View {

    private Controller controller;

    private String username;
    private long userId;

    public View () {
    }

    public void show() throws SQLException {
        this.showLoginMenu();
    }

    private void showLoginMenu() throws SQLException {
        System.out.println("\nBienvenido a nuestro chat\nPor favor, ingresa tu nombre de usuario: ");

        do {
            Scanner s = new Scanner(System.in);
            this.username = s.nextLine().trim();
        } while (username.length() == 0);

        this.controller = new Controller();

        this.userId = controller.createUser(username);

        this.showMainMenu();
    }

    private void showMainMenu() throws SQLException {

        int option;

        do {
            System.out.println("\n¡Hola " + username + "!\nElija una acción:\n [1] Acceder a una sala de chat\n [2] Crear una sala de chat\n [3] Salir");

            Scanner s = new Scanner(System.in);
            String line = s.nextLine().trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 1 || option > 3);

        switch (option) {
            case 1:  this.showAccessChatRoomMenu();
                     break;

            case 2:  this.showCreateChatRoomMenu();
                     break;

            default: System.out.println("\n¡Hasta pronto!");
        }

    }

    private void showCreateChatRoomMenu() throws SQLException {
        String name;

        do {
            System.out.println("\nEscriba el nombre de la sala de chat:");

            Scanner s = new Scanner(System.in);
            name = s.nextLine().trim();
        } while (name.length() == 0);

        controller.createChatRoom(this.userId, name);

        this.showMainMenu();
    }

    private void showAccessChatRoomMenu() throws SQLException {

        int option;

        List<ChatRoom> chatRooms = controller.getChatRooms();

        do {
            System.out.print("\nElija la sala de chat:");

            for (int i = 0; i < chatRooms.size(); i++) {
                System.out.print("\n [" + (i+1) + "] " + chatRooms.get(i).getName());
            }

            System.out.println("\n [" + (chatRooms.size()+1) + "] Volver");

            Scanner s = new Scanner(System.in);
            String line = s.nextLine().trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 0 || option > (chatRooms.size() + 1));

        if (option == (chatRooms.size()+1)) {
            this.showMainMenu();

        } else {
            ChatRoom chatRoom = chatRooms.get(option - 1);
            long chatRoomId = chatRoom.getId();
            this.showChatRoomMenu(chatRoomId);
        }
    }

    private void showChatRoomMenu(long chatRoomId) throws SQLException {
        int option;

        do {
            System.out.println("\nElija una acción:\n [1] Leer mensajes\n [2] Enviar un mensaje\n [3] Volver");

            Scanner s = new Scanner(System.in);
            String line = s.nextLine().trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 1 || option > 3);

        switch (option) {
            case 1:  this.showChatRoomMessages(chatRoomId);
                     break;

            case 2:  this.showSendMessageMenu(chatRoomId);
                     break;

            default: this.showMainMenu();
        }
    }

    private void showChatRoomMessages(long chatRoomId) throws SQLException {
        List<Message> messages = controller.getMessages(chatRoomId);

        System.out.println("\nMensajes de la sala de chat:");

        if (messages.size() == 0) {
            System.out.println("\nNo se han encontrado mensajes en esta sala de chat");
        } else {
            for (Message message : messages) {
                System.out.print("\n" + message.toString());
            }
        }

        System.out.println("\n\nPresione ENTER para continuar");

        Scanner s = new Scanner(System.in);
        s.nextLine();

        this.showChatRoomMenu(chatRoomId);
    }

    private void showSendMessageMenu(long chatRoomId) throws SQLException {

        String text;

        do {
            System.out.println("\nEscriba el contenido del mensaje:");

            Scanner s = new Scanner(System.in);
            text = s.nextLine().trim();
        } while (text.length() == 0);

        controller.sendMessage(this.userId, chatRoomId, text);

        this.showChatRoomMenu(chatRoomId);
    }
}
