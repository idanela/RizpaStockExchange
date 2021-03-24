package XmlRizpaDataExtractor;

import AutoGenerated.RizpaStockExchangeDescriptor;
import AutoGenerated.RseStock;
import AutoGenerated.RseStocks;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlRizpaDataExtractor {

    public static final String packageToReadFrom ="AutoGenerated";

   public static RizpaStockExchangeDescriptor getStocks(StringBuilder msg,String path)
   {
       RizpaStockExchangeDescriptor stocksDescriptor = null;
       try
       {
           InputStream inputStream = new FileInputStream(new File(path));
           stocksDescriptor = deserializeFrom(inputStream);
       }
       catch (JAXBException | FileNotFoundException e) {
           e.printStackTrace();
       }

       if(checkIfContentIsValid(stocksDescriptor,msg))
       {
           msg.append("File loaded successfully" + System.lineSeparator());
       }
       else
       {
           stocksDescriptor = null;
       }

       return stocksDescriptor;
   }

   private static RizpaStockExchangeDescriptor deserializeFrom(InputStream inputStream) throws JAXBException
   {
       JAXBContext jc = JAXBContext.newInstance(packageToReadFrom);
       Unmarshaller um =jc.createUnmarshaller();
       return (RizpaStockExchangeDescriptor)um.unmarshal(inputStream);
   }

   public static boolean checkIfContentIsValid(RizpaStockExchangeDescriptor rsed,StringBuilder msg)
   {   Map<String,String> companyNames = new HashMap<>();
       Map<String,RseStock> mapStocks = new HashMap<>();
       List<RseStock> stocks = rsed.getRseStocks().getRseStock();
       boolean isValidFormat = true;
       for (RseStock stock:stocks)
       {
            if(mapStocks.containsKey(stock.getRseSymbol()))
            {
                msg.append("This xml has duplicates of same symbol(stock's name)");
                isValidFormat = false;
                break;
            }

            if(companyNames.containsKey(stock.getRseCompanyName())){
                msg.append(stock.getRseCompanyName() + " company holds more than one stock");
                isValidFormat = false;
                break;
            }

            mapStocks.put(stock.getRseSymbol(),stock);
            companyNames.put(stock.getRseCompanyName(),stock.getRseCompanyName());
       }

       return isValidFormat;
   }
}
