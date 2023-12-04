<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="si"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="formBackofficeModificaCig" cssClass="form-horizontal" novalidate="novalidate" action="modificaCigBackoffice" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Backoffice Modifica CIG</h3>
					<fieldset class="form-horizontal imputazioniContabiliMovimentoGestione">
						<div class="control-group">
							<label for="annoMovimentoMovimentoGestione" class="control-label">Anno</label>
							<s:hidden id="uidImpegno" name="impegno.uid"></s:hidden>
							<s:hidden id="uidSubImpegno" name="subImpegno.uid"></s:hidden>
							<s:hidden id="numeroRemedyAssociata" name="numeroRemedyAssociata"></s:hidden>
							<div class="controls">
								<s:textfield id="annoMovimentoMovimentoGestione"
									name="impegno.annoMovimento"
									cssClass="lbTextSmall soloNumeri span2" maxlength="7"
									placeholder="%{'anno'}" />
								<span class="alRight"> <label
									for="numeroMovimentoGestione" class="radio inline">Numero</label>
								</span>
								<s:textfield id="numeroMovimentoGestione"
									name="impegno.numero" cssClass="lbTextSmall soloNumeri span2"
									maxlength="7" placeholder="%{'numero'}" />
								<span class="alRight"> <label
									for="numeroSubMovimentoGestione" class="radio inline">Subimpegno</label>
								</span>
								<s:textfield id="numeroSubMovimentoGestione"
									name="subImpegno.numero"
									cssClass="lbTextSmall soloNumeri span2" maxlength="7"
									placeholder="%{'numero subimpegno'}" />
								<span class="radio guidata">
									<button type="button" class="btn btn-info"
										id="pulsanteSelezionaImpegno">cerca</button>

									<button type="button" class="btn btn-primary"
										id="pulsanteCompilazioneGuidataMovimentoGestione">compilazione guidata</button>
										
								</span>
							</div>
						</div>
						<div class="control-group">
							<label for="numeroRemedyDaAssociare" class="control-label">Remedy</label>
							<div class="controls">
								<s:textfield id="numeroRemedyDaAssociare"
									name="numeroRemedy" cssClass="lbTextSmall span8"
									placeholder="%{'numero remedy'}" />
							</div>
						</div>
					</fieldset>

					<div id="divImpegnoCaricato" class="control-group hide">
						<div class="Border_line"></div>
						<div id="divdivImpegnoCaricato" class="accordion" data-overlay>
							<div class="accordion-group">
								<div class="accordion-heading">
									<a href="#collapseImpegnoCaricato"
										data-parent="#divImpegnoCaricato" data-toggle="collapse"
										class="accordion-toggle collapsed"> Dettagli Impegno/Subimpegno selezionato <span class="icon">&nbsp;</span>
									</a>
								</div>
								<div class="accordion-body collapse"
									id="collapseImpegnoCaricato">
									<div class="accordion-inner">
										<table class="table table-hover tab_left"
											id="tabellaModaleImpegnoCaricato">
											<thead>
												<tr>
													<th scope="col">Capitolo</th>
													<th scope="col">Provvedimento</th>
													<th scope="col">Soggetto</th>
													<th class="tab_Right" scope="col">Importo</th>
													<th class="tab_Right" scope="col">Disponibile</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td scope="col" id="tabellaModaleImpegnoCaricato_tdCapitolo"></td>
													<td scope="col" id="tabellaModaleImpegnoCaricato_tdProvvedimento"></td>
													<td scope="col" id="tabellaModaleImpegnoCaricato_tdSoggetto"></td>
													<td class="tab_Right whitespace-pre" scope="col" id="tabellaModaleImpegnoCaricato_tdImporto"></td>
													<td class="tab_Right" scope="col" id="tabellaModaleImpegnoCaricato_tdDisponibile"></td>
												</tr>
											</tbody>
										</table>
										<div class="control-group hide" id=divTabellaModaleSubImpegnoCaricato>
										<table class="table table-hover tab_left" id="tabellaModaleSubImpegnoCaricato">
											<thead>
												<tr>
													<th scope="col">Descrizione</th>
													<th scope="col">Soggetto</th>
													<th class="tab_Right" scope="col">Importo</th>
													<th class="tab_Right" scope="col">Disponibilit&agrave; a liquidare</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td scope="col" id="tabellaModaleSubImpegnoCaricato_tdDescrizione"></td>
													<td scope="col" id="tabellaModaleSubImpegnoCaricato_tdSoggetto"></td>
													<td class="tab_Right whitespace-pre" scope="col" id="tabellaModaleSubImpegnoCaricato_tdImporto"></td>
													<td class="tab_Right" scope="col" id="tabellaModaleSubImpegnoCaricato_tdDisponibilitaLiquidare"></td>
												</tr>
											</tbody>
										</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>


					<div id="divBloccoSiope" class="control-group hide" data-info-impegno>
						<div class="row">
							<span class="control-label alRight">Tipo debito SIOPE</span>
							<div class="controls">
								<s:iterator value="listaSiopeTipoDebito" var="std" status="stat">
									<label class="radio inline"> <input type="radio"
										name="siopeTipoDebito.uid"
										value="<s:property value="%{#std.uid}" />"
										data-descrizione="<s:property value="%{#std.descrizione.toUpperCase()}" />"
										<s:if test="%{siopeTipoDebito.uid == #std.uid}">checked</s:if> />
										&nbsp;<s:property value="%{#std.descrizione}" />
									</label>
								</s:iterator>
							</div>
						</div>
					</div>
					<div id="divCommerciale" class="control-group hide " data-info-impegno>
						<div class="row">
							<span class="control-label alRight">
								<label class="control-label alRight"
									for="cig"> <abbr title="codice identificativo gara">CIG</abbr>
								</label>
							</span>
							<div id="divBloccoMotivazioneAssenzaCig" class="controls">
								<s:textfield cssClass="lbTextSmall span2 cig" id="cig"
									name="cig" maxlength="10" />
								<span id="bloccoMotivazioneAssenzaCig"> <span
									class="al alRight"> <label class="radio inline"
										for="siopeAssenzaMotivazione">Motivazione assenza del
											CIG</label>
								</span> <s:select list="listaSiopeAssenzaMotivazione" headerKey=""
										headerValue="" id="siopeAssenzaMotivazione"
										name="siopeAssenzaMotivazione.uid" cssClass="span5"
										listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
								</span>
							</div>
						</div>
					</div>
					<div id="divTipoModifica" class="control-group hide">
						<div class="row">
							<span class="control-label alRight">Tipo Modifica</span>
							<div class="controls">
							<s:iterator value="listaTipoModifica" var="tm" status="stat">
								<label class="radio inline">
									<input
										type="radio"
										name="tipoModifica"
										value="<s:property value="%{#tm}" />"
										data-descrizione="<s:property value="%{#tm.descrizione}" />"
										<s:if test="%{tipoModifica == #tm}">checked</s:if> />
									&nbsp;<s:property value="%{#tm.descrizione}" />
								</label>
							</s:iterator>
							</div>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-secondary reset">annulla</button>
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="conferma" />
						</span>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/backofficeModificaCig.js"></script>

</body>
</html>