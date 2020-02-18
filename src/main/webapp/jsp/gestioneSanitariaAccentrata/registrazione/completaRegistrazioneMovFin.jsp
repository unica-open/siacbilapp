<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />

</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">	
				<s:form action="" cssClass="form-horizontal" novalidate="novalidate">
					<s:include value="/jsp/include/messaggi.jsp" />
						
					<h3>Inserimento prima nota integrata</h3>
					<h4><s:property value="intestazioneRichiesta"/></h4>
					<h4><s:property value="intestazioneMovimentoFinanziario"/></h4>
		
					<div class="step-content">
						<div class="step-pane active" id="step1">
						
						<fieldset class="form-horizontal">
							<h4 class="step-pane">Dati</h4>
				
							<div class="control-group">
								<label class="control-label">Descrizione *</label>
								<div class="controls">
									<s:textfield id="descrizionePrimaNota" name="primaNota.descrizione" cssClass="span9" required="true" /> 
							
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Data Registrazione *</label>
								<div class="controls">
									<s:textfield id="dataRegistrazionePrimaNota" name="primaNota.dataRegistrazione" cssClass="span2 datepicker" required="true" />
								</div>
							</div>
							
							
							
							<div class="control-group">
								<label class="control-label">Causale *</label>
								<div class="controls">
									<s:select list="listaCausaleEP" id="uidCausaleEP" name="causaleEP.uid"
											cssClass="span6" headerKey="" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" disabled="%{aggiornamento}"/>
								</div>
							</div>
							
							
							<div class="control-group">
								<label class="control-label">Note</label>
								<div class="controls">
									<input id="note" name="note" class="span9" type="text" value="" />
								</div>
							</div>
		
							
							<h4 class="nostep-pane">Da registrare(D-A): <span> <s:property value="daRegistrare"/></span></h4>					
							<h4 class="step-pane">Elenco scritture</h4>
					
							<table class="table table-hover tab_left" id="tabellaScritture">   				
								<thead>
									<tr>
										<th>Conto</th>
										<th>Descrizione</th>
										<th class="tab_Right">Dare</th>
										<th class="tab_Right">Avere</th>
										<th class="tab_Right span2">&nbsp;</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
								<tfoot>
									<tr>
										<th colspan="2">Totale</th>
										<th class="tab_Right" id="totaleDare"></th>
										<th class="tab_Right" id="totaleAvere"></th>
										<th class="tab_Right span2">&nbsp;</th>
									</tr>
								</tfoot>
							</table>
							<p>
								<button type="button" id="pulsanteInserimentoDati" class="btn btn-secondary">
									inserisci dati in elenco&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserimentoDati"></i>
								</button>
								<%-- <a class="btn btn-secondary" data-toggle="collapse" href="#collapseTable" aria-expanded="false" aria-controls="collapseTable">inserisci dati in elenco</a>--%>
							</p>
							<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/collapseDatiStruttura.jsp" />
									
									
									
									
							
						
		
							</fieldset>
							
						
						</div>
					</div>
					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleEliminazioneConto.jsp" />
	
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleAggiornaConto.jsp" />
	
			</div>
		</div>
	</div>

	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/registrazione/completaRegistrazioneMovFin.js"></script>
	  
</body>
</html>