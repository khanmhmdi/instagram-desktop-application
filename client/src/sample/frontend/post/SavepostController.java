package sample.frontend.post;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SavepostController
{
    @FXML
    public TextField captionField;
    private String caption = null;
    private File file = null;
    @FXML
    public Label alert;
    @FXML
    public ImageView imageView;
    @FXML
    public Button openButton;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;

    public void onFileChooserClick() throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG photo","*.png"));
        file = fileChooser.showOpenDialog(null);
        updateImageView(file);
    }

    private void updateImageView(File file) throws IOException
    {
        BufferedImage bufferedImage = ImageIO.read(file);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
    }

    public void onCancelClick()
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void onSaveClick()
    {
        caption = captionField.getText();
        if (file == null)
        {
            alert.setText("Choose a photo before saving");
        }
        else
        {
            // todo backend syncing
        }
    }
}
