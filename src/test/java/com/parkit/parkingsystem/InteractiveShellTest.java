package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.charset.Charset;

import static junit.framework.Assert.*;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private InteractiveShell interactiveShell;
    private InputStream inputStream;

    @Before
    public void setUp(){
        String command = "3";
        inputStream = new ByteArrayInputStream(command.getBytes(Charset.forName("UTF-8")));
        System.setOut(new PrintStream(outContent));
        System.setIn(inputStream);
        interactiveShell = new InteractiveShell();
    }

    @After
    public void restoreStreams(){
        System.setOut(originalOut);
    }

    @Test
    public void LoadMenuTest(){
        String expected = "Welcome to Parking System!\r\n" +
                "Please select an option. Simply enter the number to choose an action\r\n"+
                "1 New Vehicle Entering - Allocate Parking Space\r\n"+
                "2 Vehicle Exiting - Generate Ticket Price\r\n"+
                "3 Shutdown System\r\n"+
                "Exiting from the system!\r\n";
        interactiveShell.loadInterface();
        assertEquals(expected, outContent.toString());
    }
}
