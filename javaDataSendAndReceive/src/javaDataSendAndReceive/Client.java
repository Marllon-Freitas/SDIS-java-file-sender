package javaDataSendAndReceive;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

public class Client {

	public static void main(String[] args) {
		
		final File[] fileToSend = new File[1];
		
		//interface
		JFrame jframe = new JFrame("Client");
		jframe.setSize(500, 500);
		jframe.setLayout(new BoxLayout(jframe.getContentPane(), BoxLayout.Y_AXIS));
		jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
		
		JLabel jTitle = new JLabel("Enviar arquivo");
		jTitle.setFont(new Font("Arial", Font.BOLD, 25));
		jTitle.setBorder(new EmptyBorder(50, 0, 0, 0));
		jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel jFileName = new JLabel("Selecione um arquivo");
		jFileName.setFont(new Font("Arial", Font.BOLD, 20));
		jFileName.setBorder(new EmptyBorder(50, 0, 0, 0));
		jFileName.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel jButton = new JPanel();
		jButton.setBorder(new EmptyBorder(75, 0, 10, 0));
		
		//Botão de enviar o arquivo
		JButton jbSendFile = new JButton("Enviar Arquivo");
		jbSendFile.setPreferredSize(new Dimension(150, 75));
		jbSendFile.setFont(new Font("Arial", Font.BOLD, 14));
		
		//Botão de selecionar o arquivo
		JButton jbChooseFile = new JButton("Selecionar Arquivo");
		jbChooseFile.setPreferredSize(new Dimension(150, 75));
		jbChooseFile.setFont(new Font("Arial", Font.BOLD, 14));
		
		jButton.add(jbSendFile);
		jButton.add(jbChooseFile);

		jbChooseFile.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				//jfileChooser traz uma maneira mais facil de escolher aquivos
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setDialogTitle("Escolha um arquivo para enviar");
				
				//caso o usuario selecione e clique em enviar
				if (jFileChooser.showOpenDialog(null) == jFileChooser.APPROVE_OPTION) {
					//retorna o arquivo para esse objeto
					fileToSend[0] = jFileChooser.getSelectedFile();
					jFileName.setText("O arquivo " + fileToSend[0].getName() + " foi selecionado");
				}
				
			}
		});
		
		jbSendFile.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				//jfileChooser traz uma maneira mais facil de escolher aquivos
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setDialogTitle("Escolha um arquivo para enviar");
				
				//verefica se o usuário selecionou ou não o arquivo
				if (fileToSend[0] == null) {
					jFileName.setText("Por favor selecione um arquivo primeiro!");
				} else {
					//permite ler o conteudo de um arquivo
					try {
						FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
						Socket socket = new Socket("localhost", 1234);
						
						DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
						
						String fileName = fileToSend[0].getName();
						
						byte[] filenameBytes = fileName.getBytes();
						
						//conteudo em si do arquivo
						byte[] fileContentBytes = new byte[(int)fileToSend[0].length()];
						
						//lê o arquivo
						fileInputStream.read(fileContentBytes);
						
						//diz para o servidor qual o tamanho do arquivo a ser recebido e quando ele deve parar de receber
						dataOutputStream.writeInt(filenameBytes.length);
						dataOutputStream.write(filenameBytes);
						
						dataOutputStream.writeInt(fileContentBytes.length);
						dataOutputStream.write(fileContentBytes);
								
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
				
			}
		});
		
		jframe.add(jTitle);
		jframe.add(jFileName);
		jframe.add(jButton);
		jframe.setVisible(true);
	}
	

}
