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
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="cassaEconomaleCassaGestioneAggiornamento" novalidate="novalidate" id="formGestioneCassaEconomale">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Gestione casse</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<h4 class="step-pane">Dati Cassa</h4>
								<s:hidden name="cassaEconomale.uid" />
								<s:hidden name="tipoDiCassaOriginale" />
								<div class="control-group">
									<label class="control-label" for="codiceCassaEconomale">Codice</label>
									<div class="controls">
										<s:textfield id="codiceCassaEconomale" name="cassaEconomale.codice" cssClass="span3" readonly="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="descrizioneCassaEconomale">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneCassaEconomale" name="cassaEconomale.descrizione" cssClass="span9"
											required="true" maxlength="500" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="responsabileCassaEconomale">Responsabile *</label>
									<div class="controls">
										<s:textfield id="responsabileCassaEconomale" name="cassaEconomale.responsabile" cssClass="span3" required="true"
											maxlength="500" placeholder="Nome Cognome" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="tipoDiCassaCassaEconomale">Tipo cassa *</label>
									<div class="controls">
										<s:select list="listaTipoDiCassa" id="tipoDiCassaCassaEconomale" name="cassaEconomale.tipoDiCassa" cssClass="span6"
											headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="numeroContoCorrenteCassaEconomale"><abbr title="Numero">N.</abbr> Conto corrente</label>
									<div class="controls">
										<s:textfield id="numeroContoCorrenteCassaEconomale" name="cassaEconomale.numeroContoCorrente" cssClass="span9" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="limiteImportoCassaEconomale">Limite importo impegno</label>
									<div class="controls">
										<s:textfield id="limiteImportoCassaEconomale" name="cassaEconomale.limiteImporto" cssClass="span9 decimale soloNumeri" />
									</div>
								</div>
							
								<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
								<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
								<s:hidden id="HIDDEN_soggettoUid" name="cassaEconomale.soggetto.uid" />
			
								<div class="control-group">
									<label class="control-label" for="codiceSoggettoSoggetto">Codice soggetto Ragioneria</label>
									<div class="controls">
										<s:textfield id="codiceSoggettoSoggetto" cssClass="lbTextSmall span2" name="cassaEconomale.soggetto.codiceSoggetto" maxlength="20" placeholder="codice" disabled="%{isAggiornamento && !richiestaEconomalePrenotata}" />
										<span class="radio guidata">
											<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
										</span>
									</div>
								</div>
								
								<h4>Importi</h4>
								<table class="table tab_left">
									<thead>
										<tr>
											<th class="span5">&nbsp;</th>
											<th class="tab_Right"><span class="alRight">C/c</span></th>
											<th class="tab_Right"><span class="alRight">Contanti</span></th>
											<th class="tab_Right"><span class="alRight">Totale</span></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<th>Importo</th>
											<td class="tab_Right">
												<s:property value="cassaEconomale.disponibilitaCassaContoCorrenteNotNull"/>
												<s:hidden name="cassaEconomale.disponibilitaCassaContoCorrente"/>
											</td>
											<td class="tab_Right">
												<s:property value="cassaEconomale.disponibilitaCassaContantiNotNull"/>
												<s:hidden name="cassaEconomale.disponibilitaCassaContanti"/>
											</td>
											<td class="tab_Right"><s:property value="cassaEconomale.disponibilitaCassaTotale"/></td>
										</tr>
									</tbody>
								</table>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<span class="pull-right">
							<button type="button" data-toggle="modal" data-target="#modaleAnnullamentoCassaEconomale" class="btn btn-primary">annulla cassa</button>
							<s:submit cssClass="btn btn-primary" value="salva" />
						</span>
					</p>
					<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />
					<s:include value="/jsp/cassaEconomale/cassa/modaleAnnullamentoCassaEconomale.jsp" />
				</s:form>
				<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/gestioneCassa.js"></script>
	
</body>
</html>