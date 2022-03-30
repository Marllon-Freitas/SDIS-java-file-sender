package javaDataSendAndReceive;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Server {
	
	static ArrayList<MyFile> myFiles = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		
		int fileId = 0; 	
		
		JFrame jframe = new JFrame("Server");
		jframe.setSize(500, 500);
		jframe.setLayout(new BoxLayout(jframe.getContentPane(), BoxLayout.Y_AXIS));
		jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		
		JScrollPane jScroolPane = new JScrollPane(jPanel);
		jScroolPane.setVerticalScrollBarPolicy(jScroolPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JLabel jTitle = new JLabel("Arquivos recebidos");
		jTitle.setFont(new Font("Arial", Font.BOLD, 25));
		jTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
		jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		jframe.add(jTitle);	
		jframe.add(jScroolPane);	
		jframe.setVisible(true);	
		
		try (ServerSocket serverSocket = new ServerSocket(1234)) {
			//faz com que o server fique ligado pra sempre
			while(true) {
				try {
					
					Socket socket = serverSocket.accept();
					
					DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
					
					//lê do cliente o tamanho do nome do arquivo enviado
					int fileNameLength = dataInputStream.readInt();
					
					if (fileNameLength > 0) {
						byte[] fileNameBytes = new byte[fileNameLength];
						dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
						String fileName = new String(fileNameBytes);
						
						//lê do cliente o tamanho do conteudo do arquivo enviado
						int fileContentLength = dataInputStream.readInt();
						
						if (fileContentLength > 0) {
							byte[] fileContentBytes = new byte[fileContentLength];
							dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);

							//após ler o conteudo do arquivo esse arquivo é colocado na interface
							JPanel jpFileRow = new JPanel();
							jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));
							
							//junto com seu nome
							JLabel jFilenName = new JLabel(fileName);
							jFilenName.setFont(new Font("Arial", Font.BOLD, 25));
							jFilenName.setBorder(new EmptyBorder(10, 0, 10, 0));
							jFilenName.setAlignmentX(Component.CENTER_ALIGNMENT);
							
							if(getExtensionsName(fileName).equalsIgnoreCase("txt")) {
								jpFileRow.setName(String.valueOf(fileId));
								jpFileRow.addMouseListener(getMouseEvent());
								
								jpFileRow.add(jFilenName);
								jPanel.add(jpFileRow);
								jframe.validate();
							} else {
								jpFileRow.setName(String.valueOf(fileId));
								jpFileRow.addMouseListener(getMouseEvent());
								jpFileRow.add(jFilenName);
								jPanel.add(jpFileRow);
								jframe.validate();
							}
							
							myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getExtensionsName(fileName)));
							fileId++;
						}
						  
					}
					
					
					
				} catch(IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public static MouseListener getMouseEvent() {
		return new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				JPanel jPanel = (JPanel) e.getSource();
				int fileId = Integer.parseInt(jPanel.getName());
				
				for(MyFile myFiles: myFiles) {
					if(myFiles.getId() == fileId) {
						JFrame jPreview = createFrame(myFiles.getFileName(), myFiles.getFileData(), myFiles.getFileExtension());
						jPreview.setVisible(true);
					}
				}
				
			}
		};
	}
	
	public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {
		JFrame jFrame = new JFrame("Baixar o arquivo");
		jFrame.setSize(400, 400);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		
		JLabel jTitle = new JLabel("Baixar o arquivo");
		jTitle.setFont(new Font("Arial", Font.BOLD, 25));
		jTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
		jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel jPrompt = new JLabel("Você realmente deseja baixar " + fileName +"?");
		jPrompt.setFont(new Font("Arial", Font.BOLD, 25));
		jPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));
		jPrompt.setAlignmentX(Component.CENTER_ALIGNMENT); 	
		
		JButton jbYes = new JButton("Sim");
		jbYes.setPreferredSize(new Dimension(150, 75));
		jbYes.setFont(new Font("Arial", Font.BOLD, 14));
		
		JButton jbNo = new JButton("Não");
		jbNo.setPreferredSize(new Dimension(150, 75));
		jbNo.setFont(new Font("Arial", Font.BOLD, 14));
		
		JLabel jFilePreviewContent = new JLabel();
		jFilePreviewContent.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel jButtons = new JPanel();
		jButtons.setBorder(new EmptyBorder(20, 0, 10, 0));
		jButtons.add(jbYes);
		jButtons.add(jbNo);
		
		if(fileExtension.equalsIgnoreCase("txt")) {
			jFilePreviewContent.setText("<html>" + new String(fileData) + "</html>");
		} else {
			jFilePreviewContent.setIcon(new ImageIcon(fileData));
		}
		
		jbYes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File fileToDownload = new File(fileName);
				
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
					

					fileOutputStream.write(fileData);
					fileOutputStream.close();
					
					jFrame.dispose();
					
				}catch (IOException error) {
					error.printStackTrace();
				}
			}	
			
			
		});
		
		jbNo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jFrame.dispose();			
			}
		  }
		);
		
		jPanel.add(jTitle);
		jPanel.add(jPrompt);
		jPanel.add(jFilePreviewContent);
		jPanel.add(jButtons);
		
		jFrame.add(jPanel);
		
		return jFrame;
		
	}
	
	//função que pega os nomes das extenções dos arquivos
	public static String getExtensionsName(String fileName) {
		//só funciona para arquivos com uma extensãom mão funciona para arquivos com mais de um ponto em seu
		int index = fileName.lastIndexOf('.');
		if (index > 0) {
			return fileName.substring(index + 1);
		} else {
			return "Nenhuma extensão encontrada!";
		}
	}
}
