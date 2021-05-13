package xmlRizpaDataExtractor;

import generated.RizpaStockExchangeDescriptor;
import generated.RseStock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class XmlRizpaDataExtractor {

    public static final String packageToReadFrom = "generated";

   public static  RizpaStockExchangeDescriptor getStocks(String path, AtomicBoolean hasSameCompany, AtomicBoolean hasSameName)
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

       if(!checkIfContentIsValid(stocksDescriptor,hasSameCompany,hasSameName))
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

   public static boolean checkIfContentIsValid(RizpaStockExchangeDescriptor rsed, AtomicBoolean hasSameCompany, AtomicBoolean hasSameName)
   {   Map<String,String> companyNames = new HashMap<>();
       Map<String,RseStock> mapStocks = new HashMap<>();
       List<RseStock> stocks = rsed.getRseStocks().getRseStock();
       boolean isValidFormat = true;
       for (RseStock stock:stocks)
       {
            if(mapStocks.containsKey(stock.getRseSymbol()))
           hasSameName.compareAndSet(false,true) ;
            if( companyNames.containsKey(stock.getRseCompanyName()))
           hasSameCompany.compareAndSet(false,true) ;;;
            mapStocks.put(stock.getRseSymbol(),stock);
            companyNames.put(stock.getRseCompanyName(),stock.getRseCompanyName());
       }
       isValidFormat = !hasSameName.get() && !hasSameCompany.get();

       return isValidFormat;
   }
}
