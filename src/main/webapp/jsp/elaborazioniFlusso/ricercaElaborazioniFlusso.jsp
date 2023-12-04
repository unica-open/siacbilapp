<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:form action="effettuaRicercaElaborazioniFlusso" novalidate="novalidate" id="formRicercaElaborazioniFLusso">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>Ricerca PagoPA</h3>
						<p> E' necessario insrire almeno un criterio di ricerca.</p>
					<div class="step-content">
          			<div class="step-pane active" id="step1">
          			<p class="margin-medium">
					<h4>Dati principali</h4>
         		  
					<fieldset class="form-horizontal margin-large">
						<div class="control-group">
							<label class="control-label" for="Nquiet">Numero Provvisorio</label>
							<div class="controls">
								<s:textfield id="numeroProvvisorio"  name="numeroProvvisorio" cssClass="lbTextSmall span2"/>
							</div>
						</div>												
						
						<div class="control-group">
							<label class="control-label" for="flusso">Flusso</label>
							<div class="controls">
								<s:textfield id="flusso" cssClass="lbTextSmall span2" name="flusso"  />
							</div>
						</div>
						
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Data emissione</label>
							<div class="controls">
								<span class="al">
										<label class="radio inline" for="dataInizioEmissione">Inizio</label>
								</span>								
								<s:textfield id="dataInizioEmissione" title="gg/mm/aaaa" name="dataInizioEmissione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
								<span class="al">
										<label class="radio inline" for="dataFineEmissione">Fine</label>
								</span>
								<s:textfield id="dataFineEmissione" title="gg/mm/aaaa" name="dataFineEmissione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Data Elaborazione Flusso</label>
							<div class="controls">
								<span class="al">
										<label class="radio inline" for="dataElaborazioneFlusso">Inizio</label>
								</span>								
								<s:textfield id="dataInizioElaborazioneFlusso" title="gg/mm/aaaa" name="dataInizioElaborazioneFlusso" cssClass="lbTextSmall span2 datepicker"></s:textfield>
								<span class="al">
										<label class="radio inline" for="dataFineTrasmissione">Fine</label>
								</span>
								<s:textfield id="dataFineElaborazioneFlusso" title="gg/mm/aaaa" name="dataFineElaborazioneFlusso" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						
						<%-- SIAC-8046 CM 09/03/2021 Inizio--%>
						<div class="control-group">
							<span class="control-label radio-inline">Esito Elaborazione Flusso</span>
							<div class="controls">
								<s:radio id="esitoElaborazioneFlusso" style="display:initial; margin-right: 10px;" name="esitoElaborazioneFlusso" list="#{'TUTTI':'Tutti'}" checked="checked"/> <%-- Tutti i record --%>
								<s:radio id="esitoElaborazioneFlusso" style="display:initial; margin-right: 10px;" name="esitoElaborazioneFlusso" list="#{'OK':'Ok'}"/> <%-- Record con esito positivo --%>
								<s:radio id="esitoElaborazioneFlusso" style="display:initial; margin-right: 10px;" name="esitoElaborazioneFlusso" list="#{'KO':'Ko'}"/>  <%-- Record con esito negativo --%>
							</div>	
						</div>
						<%-- SIAC-8046 CM 09/03/2021 Fine--%>
											
						</fieldset>         		            
  
					</div>
				</div>
				<br/> <br/> 
				<p>
					<s:include value="/jsp/include/indietro.jsp" />
					<input type="reset" value="annulla" class="btn btn-link" />
					<input type="submit" value="cerca" class="btn btn-primary pull-right" >
				</p>
				</s:form>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/elaborazioniFlusso/ricerca.js"></script>

</body>
</html>