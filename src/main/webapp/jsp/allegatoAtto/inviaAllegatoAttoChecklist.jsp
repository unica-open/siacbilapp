<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />


<style>
.form-horizontal .control-label {
	padding-left: 50px;
	width: 500px;
	text-align: left;
}
</style>


</head>
<body>
	<%-- Inclusione header --%>
<s:include value="/jsp/include/header.jsp" />
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 ">
			<div class="contentPage">
				<s:include value="/jsp/include/messaggi.jsp" />
				<s:form action="inviaAllegatoAttoChecklist_invia" method="post" id="formInviaAllegatoAttoChecklist" 
						cssClass="form-horizontal" novalidate="novalidate">
						
				<s:hidden name="uidAllegatoAtto" />		
					<h4 class="step-pane">Atto <s:property value="descrAllegatoAtto" /></h4>
						
					<div class="fieldset">
						<div class="fieldset-group">
							<div class="fieldset-heading">
								<h4>Checklist</h4>
							</div>
							<div class="fieldset-body">
								<fieldset class="form-horizontal">

									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.soggettoCreditore.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
				    						    	<input type="radio" name="checklist.soggettoCreditore.value" value="Si" <s:if test='%{checklist.soggettoCreditore.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;    											
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.soggettoCreditore.value" value="No" <s:if test='%{checklist.soggettoCreditore.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    							<label class="radio inline">
		 											<input disabled="disabled" type="radio" name="checklist.soggettoCreditore.value" value="NonRileva" <s:if test='%{checklist.soggettoCreditore.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.sommaDovuta.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.sommaDovuta.value" value="Si" <s:if test='%{checklist.sommaDovuta.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.sommaDovuta.value" value="No" <s:if test='%{checklist.sommaDovuta.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input disabled="disabled" type="radio" name="checklist.sommaDovuta.value" value="NonRileva" <s:if test='%{checklist.sommaDovuta.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
								<%-- 	<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.causale.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.causale.value" value="Si" <s:if test='%{checklist.causale.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.causale.value" value="No" <s:if test='%{checklist.causale.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input type="radio" name="checklist.causale.value" value="NonRileva" <s:if test='%{checklist.causale.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div> --%>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.modalitaPagamento.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.modalitaPagamento.value" value="Si" <s:if test='%{checklist.causale.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.modalitaPagamento.value" value="No" <s:if test='%{checklist.causale.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input disabled="disabled" type="radio" name="checklist.modalitaPagamento.value" value="NonRileva" <s:if test='%{checklist.causale.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.scadenza.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.scadenza.value" value="Si" <s:if test='%{checklist.scadenza.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.scadenza.value" value="No" <s:if test='%{checklist.scadenza.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input type="radio" name="checklist.scadenza.value" value="NonRileva" <s:if test='%{checklist.scadenza.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.estremiProvvedimentoDirigenziale.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.estremiProvvedimentoDirigenziale.value" value="Si" <s:if test='%{checklist.estremiProvvedimentoDirigenziale.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.estremiProvvedimentoDirigenziale.value" value="No" <s:if test='%{checklist.estremiProvvedimentoDirigenziale.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input disabled="disabled" type="radio" name="checklist.estremiProvvedimentoDirigenziale.value" value="NonRileva" <s:if test='%{checklist.estremiProvvedimentoDirigenziale.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.allegati.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.allegati.value" value="Si" <s:if test='%{checklist.allegati.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.allegati.value" value="No" <s:if test='%{checklist.allegati.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input type="radio" name="checklist.allegati.value" value="NonRileva" <s:if test='%{checklist.allegati.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.esigibileSpesa.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.esigibileSpesa.value" value="Si" <s:if test='%{checklist.esigibileSpesa.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.esigibileSpesa.value" value="No" <s:if test='%{checklist.esigibileSpesa.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input type="radio" name="checklist.esigibileSpesa.value" value="NonRileva" disabled="disabled"> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.congruitaSpesaSommaImpegnata.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.congruitaSpesaSommaImpegnata.value" value="Si" <s:if test='%{checklist.congruitaSpesaSommaImpegnata.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.congruitaSpesaSommaImpegnata.value" value="No" <s:if test='%{checklist.congruitaSpesaSommaImpegnata.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input type="radio" name="checklist.congruitaSpesaSommaImpegnata.value" value="NonRileva" disabled="disabled"> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.applicazioneNormativaFiscale.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.applicazioneNormativaFiscale.value" value="Si" <s:if test='%{checklist.applicazioneNormativaFiscale.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.applicazioneNormativaFiscale.value" value="No" <s:if test='%{checklist.applicazioneNormativaFiscale.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input type="radio" name="checklist.applicazioneNormativaFiscale.value" value="NonRileva" disabled="disabled"> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.iva.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.iva.value" value="Si" <s:if test='%{checklist.iva.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.iva.value" value="No" <s:if test='%{checklist.iva.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input type="radio" name="checklist.iva.value" value="NonRileva" disabled="disabled"> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.entrataVincolata.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" id="entrataVincolataTrue" name="checklist.entrataVincolata.value" value="Si" <s:if test='%{checklist.entrataVincolata.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" id="entrataVincolataFalse" name="checklist.entrataVincolata.value" value="No" <s:if test='%{checklist.entrataVincolata.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input disabled="disabled" type="radio" id="entrataVincolataNon" name="checklist.entrataVincolata.value" value="NonRileva" <s:if test='%{checklist.entrataVincolata.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div>
												
									<div class="control-group checklist">
										<label class="control-label" for="checklist.accertamento"><s:property value="checklist.accertamento.label" /></label>
											<div class="controls">
												<s:textfield id="accertamento" cssClass="lbTextSmall span2" name="checklist.accertamento.value" maxlength="200" placeholder="N. Accertamento" />
											</div>
									</div>
												
									<div class="control-group checklist">
										<label class="control-label" for="checklist.incasso"><s:property value="checklist.incasso.label" /></label>
											<div class="controls">
												<s:textfield id="incasso" cssClass="lbTextSmall span2" name="checklist.incasso.value" maxlength="200" placeholder="N. Incasso" />
											</div>
									</div>
												
									<div class="control-group checklist">
				    					<label class="control-label"><s:property value="checklist.cig.label" /></label>
				    						<div class="controls">
				    							<label class="radio inline">   
		 											<input type="radio" name="checklist.cig.value" value="Si" <s:if test='%{checklist.cig.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
				    						    </label>
				    						    <label class="radio inline">
				    						    	<input type="radio" name="checklist.cig.value" value="No" <s:if test='%{checklist.cig.value.name() eq "No"}'>checked="checked"</s:if>> No   
				    						    </label>
				    						  	<label class="radio inline">
		 											<input type="radio" name="checklist.cig.value" value="NonRileva" <s:if test='%{checklist.cig.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
				    						    </label>
				    						</div>
									</div>
										
										<div class="control-group checklist">
		    								<label class="control-label"><s:property value="checklist.cup.label" /></label>
		    								<div class="controls">
		    									<label class="radio inline">   
 													<input type="radio" name="checklist.cup.value" value="Si" <s:if test='%{checklist.cup.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
		    								    </label>
		    								    <label class="radio inline">
		    								    	<input type="radio" name="checklist.cup.value" value="No" <s:if test='%{checklist.cup.value.name() eq "No"}'>checked="checked"</s:if>> No   
		    								    </label>
		    								  	<label class="radio inline">
 													<input type="radio" name="checklist.cup.value" value="NonRileva" <s:if test='%{checklist.cup.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
		    								    </label>
		    								</div>
										</div>
										
										<div class="control-group checklist">
		    								<label class="control-label"><s:property value="checklist.contributiva.label" /></label>
		    								<div class="controls">
		    									<label class="radio inline">   
 													<input type="radio" name="checklist.contributiva.value" value="Si" <s:if test='%{checklist.contributiva.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
		    								    </label>
		    								    <label class="radio inline">
		    								    	<input type="radio" name="checklist.contributiva.value" value="No" disabled="disabled"> No   
		    								    </label>
		    								  	<label class="radio inline">
 													<input type="radio" name="checklist.contributiva.value" value="NonRileva" <s:if test='%{checklist.contributiva.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
		    								    </label>
		    								</div>
										</div>
										
										<div class="control-group checklist">
		    								<label class="control-label"><s:property value="checklist.pubblicazione.label" /></label>
		    								<div class="controls">
		    									<label class="radio inline">   
 													<input type="radio" name="checklist.pubblicazione.value" value="Si" <s:if test='%{checklist.pubblicazione.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
		    								    </label>
		    								    <label class="radio inline">
		    								    	<input type="radio" name="checklist.pubblicazione.value" value="No" <s:if test='%{checklist.pubblicazione.value.name() eq "No"}'>checked="checked"</s:if>> No   
		    								    </label>
		    								  	<label class="radio inline">
 													<input type="radio" name="checklist.pubblicazione.value" value="NonRileva" <s:if test='%{checklist.pubblicazione.value.name() eq "NonRileva"}'>checked="checked"</s:if>> Non Rileva  
		    								    </label>
		    								</div>
										</div> 
										
								<%-- 		<div class="control-group checklist">
		    								<label class="control-label"><s:property value="checklist.regolaritaContabile.label" /></label>
		    								<div class="controls">
		    									<label class="radio inline">   
 													<input type="radio" name="checklist.regolaritaContabile.value" value="Si" <s:if test='%{checklist.regolaritaContabile.value.name() eq "Si"}'>checked="checked"</s:if>> S&igrave;   
		    								    </label>
		    								    <label class="radio inline">
		    								    	<input type="radio" name="checklist.regolaritaContabile.value" value="No" <s:if test='%{checklist.regolaritaContabile.value.name() eq "No"}'>checked="checked"</s:if>> No   
		    								    </label>
		    								  	<label class="radio inline">
 													<input type="radio" name="checklist.regolaritaContabile.value" value="NonRileva" disabled="disabled"> Non Rileva  
		    								    </label>
		    								</div>
										</div> --%>
										
									</fieldset>
									<p>
										<s:include value="/jsp/include/indietro.jsp" />
										
										<s:submit cssClass="btn btn-primary pull-right" value="invia" />
									</p>
								</div>
							</div>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/inviaAllegatoAttoChecklist.js"></script>	

</body>
</html>