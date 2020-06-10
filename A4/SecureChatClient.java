//client im writing
//SecureChatClient
import java.math.*;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SecureChatClient extends JFrame implements Runnable, ActionListener 
{
	
    public static final int PORT = 8765;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
    Socket connection;
	private byte[] securedName;
    private byte[] securedMessage;
    private byte[] securedClosingMsg;
    ObjectOutputStream currWriter;
    ObjectInputStream currReader;
    BigInteger E; //public key
    BigInteger N; //mod value N
    String cipherName;
    SymCipher cipher;
    BigInteger key;
    BigInteger secureKey;
	
  

    public SecureChatClient()
    {
        try 
        {
            //i/o handling via jPane...
            System.out.println("Starting client...");
            myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
            InetAddress addr = InetAddress.getByName(serverName);
			
            //client connects to server 
            connection = new Socket(addr, PORT);
			//It opens a connection to the server via a Socket at the server's IP address and port.
            currWriter = new ObjectOutputStream(connection.getOutputStream());
			//It creates an ObjectOutputStream on the socket (for writing) 
            currWriter.flush(); //and immediately calls the flush() method (this technicality prevents deadlock)
            currReader = new ObjectInputStream(connection.getInputStream()); //It creates on ObjectInputStream on the socket 
            E = (BigInteger)currReader.readObject(); //It receives the server's public key, E, as a BigInteger object
            N = (BigInteger)currReader.readObject(); //It receives the server's public mod value, N, as a BigInteger object
            System.out.println("\nE key Received: " + E);
            System.out.println("\nN key Received: " + N);
            cipherName = (String)currReader.readObject(); //It receives the server's preferred symmetric cipher (either "Sub" or "Add"), as a String object
            if(cipherName.equals("Sub")==true) //Based on the value of the cipher preference, it creates either a Substitute object
            {
                cipher = new Substitute(); //storing the resulting object in a SymCipher variable.
                System.out.println("\nSymmetric Encryption Scheme: Subsitute()");
            }
            else //or an Add128 object
            {
                cipher = new Add128(); //storing the resulting object in a SymCipher variable.
                System.out.println("\nSymmetric Encryption Scheme: Add128()");
            }
            key = new BigInteger(1, cipher.getKey()); //It gets the key from its cipher object using the getKey() method, and then converts the result into a BigInteger object. The 1 ensures a pos num.
            secureKey = key.modPow(E, N);//It RSA-encrypts the BigInteger version of the key using E and N
            currWriter.writeObject(secureKey); //sends the resulting BigInteger to the server (so the server can also determine the key – the server already knows which cipher will be used)
            currWriter.flush();
            securedName = cipher.encode(myName); //It prompts the user for name, then encrypts it using the cipher and sends it to the server.
			//The encryption will be done using the encode() method of the SymCipher interface, and the resulting array of bytes will be sent to the server as a single object using the ObjectOutputStream.
            
			currWriter.writeObject(securedName);   // Send name to Server. 
            currWriter.flush(); //prevents deadlock
            System.out.println("\n<<HANDSHAKE ESTABLISHED>>\n");
            
            this.setTitle(myName);      // Set title to identify chatter

            Box b = Box.createHorizontalBox();  // Set up graphical environment for user
            outputArea = new JTextArea(8, 30);  
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Group, " + myName + "\n");

            inputField = new JTextField("");  // This is where user will type their input
            inputField.addActionListener(this);

            prompt = new JLabel("Type your messages below:");
            Container c = getContentPane();

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  // Thread is to receive strings from Server
            outputThread.start();                    

            addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e) //encrypt and transmit close message
                    { 
                        securedClosingMsg = cipher.encode("CLIENT CLOSING");
                        try 
                        {
                            System.out.println("TRANSMITTING CLOSE SIGNAL");
                            currWriter.writeObject(securedClosingMsg);
                            currWriter.flush();
                        } 
                        catch (IOException e1) 
                        {
                            e1.printStackTrace();
                        }
                        System.exit(0);
                    }
                }
             );

             setSize(500, 200);
             setVisible(true);
        }
    catch (Exception e)
    {
     System.out.println("Problem starting client!");
    }
    }

    public void run()
    {
    while (true)
    {
      try 
      {         
          byte[] encryptedMsg = (byte[])currReader.readObject();
          encryptedMsg = (cipher.decode(encryptedMsg)).getBytes();
          String msg = new String(encryptedMsg);
          outputArea.append(msg+"\n");
      }
      catch (Exception e)
      {
         System.out.println(e +  ", CLOSING CLIENT");
         break;
      }
    }
    System.exit(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        String userName = myName + ": ";
        String currMsg = userName + e.getActionCommand(); // get input value
        securedMessage = cipher.encode(currMsg); // encode input value
        inputField.setText("");
        try 
        {
            currWriter.writeObject(securedMessage);
            currWriter.flush();
        } 
        catch (IOException e1) 
        {
            System.out.println("writing failed!!!");
        }   // Add name and send it to Server
    }                                               

    public static void main(String [] args)
    {
    SecureChatClient JR = new SecureChatClient();
    JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}