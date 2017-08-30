package webTests;

import static java.lang.System.setProperty;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import model.FileInfo;
import model.Vehicle;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import service.InfoDeriver;

import com.opencsv.CSVReader;

public class VehicleMakeAndColorTest {
    
    private static final Logger LOGGER= Logger.getLogger(VehicleMakeAndColorTest.class.getName());
    @Test
    public void testVehicleDetailsAreAuthentic() {
        VehicleMakeAndColorTest vehicles= new VehicleMakeAndColorTest();
        List<Vehicle> listOfVehicles = vehicles.getListOfVehicles();
        listOfVehicles.forEach(vehicle->{;
        verifyVehicleDetails(vehicle);
        });
    }

    private void verifyVehicleDetails(Vehicle vehicle) {
        String exePath = new File("").getAbsolutePath()+"\\chromedriver.exe";
        setProperty("webdriver.chrome.driver", exePath);
        WebDriver driver= new ChromeDriver();
        driver.get("https://www.gov.uk/get-vehicle-information-from-dvla");
        driver.findElement(className("button")).click();
        driver.findElement(xpath("/html/body[@class='js-enabled']/main[@id='content']/form[@class='form']/div[@class='grid-row no-bottom']/div[@class='column-two-thirds no-bottom']/div[@class='form-group no-bottom']/fieldset/div[@class='form-group field-input-validation-Vrm']/input[@id='Vrm']")).sendKeys(vehicle.getRegistrationNumber());
        driver.findElement(xpath("/html/body[@class='js-enabled']/main[@id='content']/form[@class='form']/div[@class='grid-row no-bottom']/div[@class='column-two-thirds no-bottom']/div[@class='form-group no-bottom']/fieldset/button[@class='button']")).click();
        WebElement carMake = driver.findElement(xpath("/html/body[@class='js-enabled']/main[@id='content']/form[@class='form']/div[@id='pr3']/div[@class='column-two-thirds']/ul[@class='list-summary margin-bottom-2']/li[@class='list-summary-item'][2]/span[2]/strong"));
        WebElement carColour = driver.findElement(xpath("/html/body[@class='js-enabled']/main[@id='content']/form[@class='form']/div[@id='pr3']/div[@class='column-two-thirds']/ul[@class='list-summary margin-bottom-2']/li[@class='list-summary-item'][3]/span[2]/strong"));
        assertEquals(vehicle.getMake(), carMake.getText());
        assertEquals(vehicle.getColour(), carColour.getText());
        driver.close();
    }
    
    public  List<Vehicle> getListOfVehicles(){
        List<Vehicle> vehicleList= new ArrayList<>();
        InfoDeriver infoDeriver= new InfoDeriver();
        List<FileInfo> retrieveInfoForFilesInDirectory = infoDeriver.retrieveInfoForFilesInDirectory(new File((new File("").getAbsolutePath()+"/config_dir/")));
        List<File> permittedFiles = infoDeriver.getFilesWithPermittedMimeTypes(retrieveInfoForFilesInDirectory);
        permittedFiles.forEach(i->{
            if(i.length()>0L){
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(i.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            LOGGER.info("Unable to parse file. Please check the file exists");
        }
        try {
            if(getExtension(i.getName()).equals("csv")){
            List<String[]> myEntries = reader.readAll();
            IntStream.range(1,myEntries.size()).forEach(index->{
            Vehicle vehicle= new Vehicle();    
            vehicle.setRegistrationNumber(myEntries.get(index)[0]);
            vehicle.setMake(myEntries.get(index)[1]);
            vehicle.setColour(myEntries.get(index)[2]);
            vehicleList.add(vehicle);
            });
            }
            else{
            }
        } catch (IOException e) {
            LOGGER.info("Can't read file");
        }
            }
        });
        return vehicleList;
    }
}


