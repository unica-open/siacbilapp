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
				<s:include value="/jsp/include/messaggi.jsp" />
				<h3>Inserimento storni UEB</h3>
				<div class="accordion margin-large" id="accordion">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseb">
								<i class="icon-pencil icon-2x"></i>&nbsp;UEB sorgente: <s:property value="numeroCapitolo" /> / <s:property value="numeroArticolo" /> / <s:property value="numeroUEBSorgente" /><span class="icon"></span>
							</a>
						</div>
						<div id="collapseb" class="accordion-body collapse in">
							<div class="accordion-inner">
								<h4 id="disponibilitaCapitoloSorgente">
									UEB <s:property value="numeroCapitolo"/>/<s:property value="numeroArticolo"/>/<s:property value="numeroUEBSorgente"/> - Disponibilit&agrave;: <s:property value="disponibilitaSorgente"/>
								</h4>
								
								<table summary="riepilogo incarichi" class="table table-hover">
									<tr>
										<th>&nbsp;</th>
										<th><s:property value="annoEsercizioInt"/></th>
										<th><s:property value="%{annoEsercizioInt + 1}"/></th>
										<th><s:property value="%{annoEsercizioInt + 2}"/></th>
									</tr>
									<tr>
										<th>Competenza</th>
										<td>
											<s:textfield id="competenzaSorgente0" cssClass="input-small" required="true" name="stanziamentoCompetenzaSorgente0" disabled="true" />
											<label for="competenzaSorgente0" class="nascosto">inserisci importo</label>
										</td>
										<td>
											<s:textfield id="competenzaSorgente1" cssClass="input-small" required="true" name="stanziamentoCompetenzaSorgente1" disabled="true" />
											<label for="competenzaSorgente1" class="nascosto">inserisci importo</label>
										</td>
										<td>
											<s:textfield id="competenzaSorgente2" cssClass="input-small" required="true" name="stanziamentoCompetenzaSorgente2" disabled="true" />
											<label for="competenzaSorgente2" class="nascosto">inserisci importo</label>
										</td>
									</tr>
									<tr>
										<th>Cassa</th>
										<td>
											<s:textfield id="cassaSorgente0" cssClass="input-small" required="true" name="stanziamentoCassaSorgente0" disabled="true" />
											<label for="cassaSorgente0" class="nascosto">inserisci importo</label>
										</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
				
				<div class="accordion margin-large" id="accordioncc">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordioncc" href="#collapsec">
								<i class="icon-pencil icon-2x"></i>&nbsp;UEB destinazione: <s:property value="numeroCapitolo" /> / <s:property value="numeroArticolo" /> / <s:property value="numeroUEBDestinazione" /><span class="icon"></span>
							</a>
						</div>
						<div id="collapsec" class="accordion-body collapse">
							<div class="accordion-inner">
								<h4 id="disponibilitaCapitoloDestinazione">
									UEB <s:property value="numeroCapitolo"/>/<s:property value="numeroArticolo"/>/<s:property value="numeroUEBDestinazione"/> - Disponibilit&agrave;: <s:property value="disponibilitaDestinazione"/>
								</h4>
							</div>
						</div>
					</div>
				</div>

				<div class="accordion margin-large" id="accordion5">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion4" href="#collapse4">
								<i class="icon-pencil icon-2x"></i>&nbsp;provvedimento: <s:property value="annoProvvedimento"/> / <s:property value="numeroProvvedimento"/><span class="icon"></span>
							</a>
						</div>
						<div id="collapse4" class="accordion-body collapse">
							<div class="accordion-inner">
								<fieldset class="form-horizontal" id="formRicercaProvvedimento">
									<div class="control-group">
										<label class="control-label" for="annoProvvedimento">Anno *</label>
										<div class="controls">
											<s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1" name="attoAmministrativo.anno" disabled="true" />
											<span class="al">
												<label class="radio inline" for="numeroProvvedimento">Numero</label>
											</span>
											<s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span1" name="numeroProvvedimento" disabled="true" />
											<span class="al">
												<label class="radio inline" for="tipoAttoProvvedimento">Tipo</label>
											</span>
											<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid" id="tipoAttoProvvedimento" cssClass="lbTextSmall span2"
												headerKey="" headerValue="" disabled="true" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="attoAmministrativo.strutturaAmmContabile.uid" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="strutturaAmministrativoContabile">Struttura Amministrativa</label>
										<div class="controls">
											<s:property value="%{attoAmministrativo.strutturaAmmContabile.codice + '-' + attoAmministrativo.strutturaAmmContabile.descrizione}" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="oggettoProvvedimento">Oggetto</label>
										<div class="controls">
											<s:textfield id="oggettoProvvedimento" cssClass="lbTextSmall span9" name="attoAmministrativo.oggetto" maxlength="500" disabled="true" />
										</div>
									</div>
								</fieldset>
							</div>
						</div>
					</div>
				</div>
				<s:hidden id="HIDDEN_uidProvvedimento" name="uidProvvedimento" />
				<s:hidden id="HIDDEN_numeroProvvedimento" name="numeroProvvedimento" />
				<s:hidden id="HIDDEN_annoProvvedimento" name="annoProvvedimento" />

				<p class="margin-large">
					<s:include value="/jsp/include/indietro.jsp" />
				</p>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>