package com.icat.epicenter.pmd;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTEqualityExpression;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNullLiteral;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.properties.StringMultiProperty;
@SuppressWarnings("deprecation")
public class BannedEqualsEqualsCheck extends AbstractJavaRule{
	
	StringMultiProperty bannedClasses;
	public BannedEqualsEqualsCheck() {
		bannedClasses= StringMultiProperty.named("bannedClasses").desc("Classes where an == comparison should be banned").defaultValues(String.class.getName()).build();
		definePropertyDescriptor(bannedClasses);
	}
	Set<Class<?>> bannedEqualsClasses = new HashSet<>();
	@Override
	 public Object visit(ASTEqualityExpression node, Object data) {
		List<String> classes = getProperty(bannedClasses);
		Node c0 = node.jjtGetChild(0).jjtGetChild(0);
        Node c1 = node.jjtGetChild(1).jjtGetChild(0);
		if(!isNodeNull(c0) && !isNodeNull(c1) && (isNodeOfAnyGivenType(c0, classes) || isNodeOfAnyGivenType(c1, classes)))
			addViolation(data, node);
		
		return data;
		 
	 }
	
	private boolean isNodeNull(Node node) {
		if(node.jjtGetNumChildren()==1 && (node.jjtGetChild(0) instanceof ASTLiteral) && node.jjtGetChild(0).hasDescendantOfType(ASTNullLiteral.class))
			return true;
		return false;
	}
	
	private boolean isNodeOfAnyGivenType(Node node, final Collection<String> stringClassNames) {
		if(node instanceof AbstractJavaTypeNode) {
			AbstractJavaTypeNode ast = (AbstractJavaTypeNode) node;
			if(ast.getTypeDefinition()!=null && 
				 ast.getFirstChildOfType(ASTName.class) != null &&  
				 ast.getFirstChildOfType(ASTName.class).jjtGetFirstToken() == ast.getFirstChildOfType(ASTName.class).jjtGetLastToken() &&
				ast.getTypeDefinition().getErasedSuperTypeSet().stream().anyMatch(a->stringClassNames.contains(a.getName()))){
				return true;
			}
		}
		return false;
	}
}
