<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<div aria-hidden="true" aria-labelledby="labelmodaleVariazioneTassoMutuiSelezionati" role="dialog" tabindex="-1"
		class="modal hide fade"id="modaleVariazioneTassoMutuiSelezionati">
	<div class="modal-body">
        <h4>Variazione tasso Euribor</h4>
		<fieldset class="form-horizontal" id="FIELDSET_modaleVariazioneTassoMutuiSelezionati">
			<div class="control-group">
				<p><strong>Elementi selezionati: <span id="spanModaleVariazioneTassoMutuiSelezionati"></span></strong></p>
				<label class="control-label">Euribor</label>
				<div class="controls">
            		<s:textfield type="text" id="tassoInteresseEuribor" class="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale" 
					   name="tassoInteresseEuribor" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
		<button type="button" class="btn btn-primary selezionati" id="buttonVariazioneTassoMutuiSelezionati" >
			s&igrave;, prosegui
		  </button>
	</div>
</div>