package org.jqassistant.contrib.plugin.c.api.model;

import java.util.List;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Variable")
public interface VariableDescriptor extends CDescriptor, NamedDescriptor, DependsOnDescriptor, SourceFileDescriptor{

	/**
	 * Returns the type specifiers and qualifiers of this variable
	 * @return the type specifiers and qualifiers
	 */
	@Relation("OF_TYPE")
	TypeDescriptor getTypeSpecifiers();
	void setTypeSpecifiers(TypeDescriptor typeSpecifiers);
	
	List<WritesDescriptor> getWrittenBy();

	List<ReadsDescriptor> getReadBy();
}
