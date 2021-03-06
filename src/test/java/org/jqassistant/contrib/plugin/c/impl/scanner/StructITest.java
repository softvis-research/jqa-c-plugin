package org.jqassistant.contrib.plugin.c.impl.scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jqassistant.contrib.plugin.c.api.model.CAstFileDescriptor;
import org.jqassistant.contrib.plugin.c.api.model.CDescriptor;
import org.jqassistant.contrib.plugin.c.api.model.FunctionDescriptor;
import org.jqassistant.contrib.plugin.c.api.model.SingleConditionDescriptor;
import org.jqassistant.contrib.plugin.c.api.model.StructDescriptor;
import org.jqassistant.contrib.plugin.c.api.model.TranslationUnitDescriptor;
import org.jqassistant.contrib.plugin.c.api.model.UnionDescriptor;
import org.jqassistant.contrib.plugin.c.api.model.VariableDescriptor;
import org.junit.Test;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;

public class StructITest extends AbstractPluginIT{

	@Test
	public void testStructDeclaration() {
		store.beginTransaction();
        
        File testFile = new File(getClassesDirectory(StructITest.class), "/struct.ast");
        Scanner scanner = getScanner();
        CAstFileDescriptor fileDescriptor = store.create(CAstFileDescriptor.class);
        Descriptor returnedDescriptor = scanner.scan(testFile, fileDescriptor, testFile.getAbsolutePath(), DefaultScope.NONE);

        CAstFileDescriptor descriptor = (CAstFileDescriptor) returnedDescriptor;
        TranslationUnitDescriptor translationUnitDescriptor = descriptor.getTranslationUnit();
        List<StructDescriptor> structList = translationUnitDescriptor.getDeclaredStructs();
        assertEquals(6, structList.size());
        List<String> structNameList = new ArrayList<>();
        structNameList.add("adresse");
        structNameList.add("adresse1");
        for(CDescriptor cDescriptor : structList) {
        	if(cDescriptor instanceof StructDescriptor) {
        		StructDescriptor struct = (StructDescriptor) cDescriptor;
        		assertEquals("struct.c", struct.getFileName());
        		assertTrue(structNameList.contains(struct.getName()));
        		assertEquals(5, struct.getDeclaredVariables().size());
        		for(VariableDescriptor variable : struct.getDeclaredVariables()) {
        			assertEquals("struct.c", variable.getFileName());
        			switch (variable.getName()) {
					case "name":
						assertEquals("char [50]", variable.getTypeSpecifiers().getName());
						break;
					case "strasse":
						assertEquals("char [100]", variable.getTypeSpecifiers().getName());
						break;
					case "hausnummer":
						assertEquals("short", variable.getTypeSpecifiers().getName());
						break;
					case "plz":
						assertEquals("long", variable.getTypeSpecifiers().getName());
						break;
					case "stadt":
						assertEquals("char [50]", variable.getTypeSpecifiers().getName());
						break;
					default:
						break;
					}
        		}
        	} else if(cDescriptor instanceof VariableDescriptor) {
        		VariableDescriptor variable = (VariableDescriptor) cDescriptor;
        		assertEquals("struct.c", variable.getFileName());
        		List<String> variableNameList = new ArrayList<>();
        		variableNameList.add("newAddress");
        		variableNameList.add("adresse2");
        		variableNameList.add("addressbook");
        		assertTrue(variableNameList.contains(variable.getName()));
        		if(variable.getName().equals("newAddress")) {
        			assertEquals("struct adresse", variable.getTypeSpecifiers().getName());
        		} else if(variable.getName().equals("addressbook")){
        			assertEquals("struct adresse [20]", variable.getTypeSpecifiers().getName());
        		} else {
        			assertEquals("struct adresse1", variable.getTypeSpecifiers().getName());
        		}
        	}
        }
        
        store.commitTransaction();
	}
	
	@Test
	public void testConditionalStruct() {
		store.beginTransaction();
        
        File testFile = new File(getClassesDirectory(StructITest.class), "/conditional_struct.ast");
        Scanner scanner = getScanner();
        CAstFileDescriptor fileDescriptor = store.create(CAstFileDescriptor.class);
        Descriptor returnedDescriptor = scanner.scan(testFile, fileDescriptor, testFile.getAbsolutePath(), DefaultScope.NONE);

        CAstFileDescriptor descriptor = (CAstFileDescriptor) returnedDescriptor;
        TranslationUnitDescriptor translationUnitDescriptor = descriptor.getTranslationUnit();
        List<StructDescriptor> structList = translationUnitDescriptor.getDeclaredStructs();
        assertEquals(2, structList.size());
        
        for(CDescriptor cDescriptor : structList) {
        	if(cDescriptor instanceof StructDescriptor) {
        		StructDescriptor struct = (StructDescriptor) cDescriptor;
        		assertEquals("conditional_struct.c", struct.getFileName());
        		assertEquals(5, struct.getDeclaredVariables().size());
        		for(VariableDescriptor variable : struct.getDeclaredVariables()) {
        			switch (variable.getName()) {
					case "name":
						assertEquals("char [50]", variable.getTypeSpecifiers().getName());
						break;
					case "strasse":
						assertEquals("char [100]", variable.getTypeSpecifiers().getName());
						break;
					case "hausnummer":
						assertEquals("short", variable.getTypeSpecifiers().getName());
						break;
					case "plz":
						assertEquals("long", variable.getTypeSpecifiers().getName());
						break;
					case "stadt":
						assertEquals("char [50]", variable.getTypeSpecifiers().getName());
						break;
					default:
						break;
					}
        		}
        		assertNotNull(struct.getCondition());
        		assertEquals("FLAG", ((SingleConditionDescriptor) struct.getCondition()).getMacroName());
        	}
        }
        
        store.commitTransaction();
	}

	@Test
	public void testNestedStruct() throws Exception {
		store.beginTransaction();
        
        File testFile = new File(getClassesDirectory(StructITest.class), "/struct_union_nested.ast");
        Scanner scanner = getScanner();
        CAstFileDescriptor fileDescriptor = store.create(CAstFileDescriptor.class);
        Descriptor returnedDescriptor = scanner.scan(testFile, fileDescriptor, testFile.getAbsolutePath(), DefaultScope.NONE);

        CAstFileDescriptor descriptor = (CAstFileDescriptor) returnedDescriptor;
        TranslationUnitDescriptor translationUnitDescriptor = descriptor.getTranslationUnit();
        List<StructDescriptor> structList = translationUnitDescriptor.getDeclaredStructs();
        assertEquals(3, structList.size());
        
        for(CDescriptor cDescriptor : structList) {
        	if(cDescriptor instanceof StructDescriptor) {
        		StructDescriptor struct = (StructDescriptor) cDescriptor;
        		assertEquals("struct_union_nested.c", struct.getFileName());
        		assertEquals(1, struct.getDeclaredUnions().size());
        		for(UnionDescriptor union : struct.getDeclaredUnions()) {
            		assertEquals("struct_union_nested.c", union.getFileName());
        			List<VariableDescriptor> unionContentList = union.getDeclaredVariables();
        			assertEquals(8, unionContentList.size());
        			for(CDescriptor unionContentDescriptor : unionContentList) {
        				if(unionContentDescriptor instanceof StructDescriptor) {
        					StructDescriptor structInUnion = (StructDescriptor) unionContentDescriptor;
        	        		assertEquals("struct_union_nested.c", structInUnion.getFileName());
        					assertEquals(4, structInUnion.getDeclaredVariables().size());
        					assertEquals(null, structInUnion.getName());
        					for(VariableDescriptor structVariable : structInUnion.getDeclaredVariables()){
        		        		assertEquals("struct_union_nested.c", structVariable.getFileName());
        						switch (structVariable.getName()) {
								case "name":
									assertEquals("char [20]", structVariable.getTypeSpecifiers().getName());
									break;
								case "power":
									assertEquals("int", structVariable.getTypeSpecifiers().getName());
									break;
								case "leben":
									assertEquals("unsigned char", structVariable.getTypeSpecifiers().getName());
									break;
								case "geschwindigkeit":
									assertEquals("unsigned int", structVariable.getTypeSpecifiers().getName());
									break;
								default:
									throw new Exception("line 183: case not possible:" + structVariable.getName());
								}
        					}
        				} else if(unionContentDescriptor instanceof VariableDescriptor) {
        					VariableDescriptor unionContentVariable = (VariableDescriptor) unionContentDescriptor;
        					assertTrue(unionContentVariable.getName().startsWith("fighter"));
        					assertTrue(unionContentVariable.getTypeSpecifiers().getName().equals("struct"));
        	        		assertEquals("struct_union_nested.c", unionContentVariable.getFileName());
        				}
        			}
        			
        		}
        	} else if(cDescriptor instanceof VariableDescriptor) {
        		VariableDescriptor variable = (VariableDescriptor) cDescriptor;
        		assertEquals("dat", variable.getName());
        		assertEquals("struct gegner", variable.getTypeSpecifiers().getName());
        	} else if(cDescriptor instanceof FunctionDescriptor) {
        		assertEquals("main", ((FunctionDescriptor) cDescriptor).getName());
        	}
        }
        
        store.commitTransaction();
	}
}
