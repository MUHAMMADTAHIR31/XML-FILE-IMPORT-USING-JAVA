import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
 
import java.awt.*;
import java.io.File;
import java.awt.event.*;
import javax.swing.*;

 
public class XMLTableModel extends JFrame implements ActionListener {
	
    public static void main(String[] args) {
        new XMLTableModel();
    }
    
    public File xml = null;
    public Document dom = null; 
    public JScrollPane jScrollPane1;
    public DefaultTableModel model;
    public JTable jTable1;
	public JButton open = new JButton("OPEN");
	public JButton view = new JButton("Import");
    Container con;
	JFileChooser  FileChooser = new JFileChooser();
	
	
    public XMLTableModel() {
        
		con=this.getContentPane();
		con.setLayout(new BorderLayout());
		
		this.setBounds(10,10,700,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		
		model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        //creates an instance of the table class and sets it up in a scrollpane
        jTable1 = new JTable(model);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		jTable1.setBounds(30,40,200,300);     
        jScrollPane1 = new JScrollPane(jTable1);            
        add(jScrollPane1);
    
        //add some columns
        model.addColumn("Roll number");
        model.addColumn("Name");
        model.addColumn("Father Name");
        model.addColumn("SurName");
		
		JPanel southPanel = new JPanel();
        southPanel.add(open);
        southPanel.add(view);
        add(southPanel, BorderLayout.SOUTH);
	   
		open.addActionListener(this);
		view.addActionListener(this);
		
    }
   
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource()==open){
			int returnVal = 0;
			FileChooser.setCurrentDirectory( new File( "D:" ) );
			FileChooser.showOpenDialog( (Component) evt.getSource()   );
			if ( returnVal == FileChooser.APPROVE_OPTION ) {
				String filePath =  FileChooser.getSelectedFile().getAbsolutePath();
				xml = new File( filePath );
				if (xml.exists() && xml.length() != 0) {
					dom = parseFile(xml);
					insertTableRows(model,dom);
				}       
			}
		}
	}
	
    //creates an instance of a Document object  
    public Document parseFile(File file) {
        try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();     
        dom = (Document) builder.parse(file);
        } catch (Exception e) { e.printStackTrace(); }  
        return dom;
    }
   
    public void insertTableRows(DefaultTableModel tableModel,Document doc) {            
        Element root = doc.getDocumentElement();
        NodeList list = root.getElementsByTagName("student");
        for (int i = 0; i < list.getLength(); ++i) {
            Element e = (Element) list.item(i);
            if (e.getNodeType() == Element.ELEMENT_NODE) {
                Object[] row = { getArticleInfo("rollnumber",e),getArticleInfo("name",e),getArticleInfo("fname",e),getArticleInfo("surname",e) };
                tableModel.addRow(row);             
            }
        }       
         
        tableModel.fireTableStructureChanged(); 
        tableModel.fireTableDataChanged();
    }
     
    public Object getArticleInfo(String tagName, Element elem) {    
        NodeList list = elem.getElementsByTagName(tagName);
        for (int i = 0; i < list.getLength(); ++i) {
            Node node = (Node) list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Node child = (Node) node.getFirstChild();
                return child.getTextContent().trim();
            }
            return null;
        }
        return null;
    }
} 