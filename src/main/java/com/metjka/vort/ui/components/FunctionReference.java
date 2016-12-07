package com.metjka.vort.ui.components;

import com.metjka.vort.ui.BlockContainer;
import javafx.scene.layout.Region;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FunctionReference {
    
    void initializeBlock(Block funBlock);
    
    Optional<InputAnchor> getInputAnchor();
    
    int requiredArguments();
    
    Type refreshedType(int argCount, TypeScope scope);
    
    Expression getLocalExpr(Set<OutputAnchor> outsideAnchors);

    void invalidateVisualState();
    
    Region asRegion();
    
    FunctionReference getNewCopy();
    
    String getName();
    
    boolean isScopeCorrectIn(BlockContainer container);

    void deleteLinks();

    Map<String, Object> toBundleFragment();
}
