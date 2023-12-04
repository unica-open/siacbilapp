<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
				<s:form id="formInserimentoAllegatoAttoStep2" cssClass="form-horizontal" novalidate="novalidate" action="inserisciAllegatoAtto_redirezioneAggiornamentoAllegatoAtto" method="post">
					<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="denominazioneAllegatoAtto"/></h3>
					<s:hidden id="HIDDEN_uidAllegatoAtto" name="allegatoAtto.uid" />
					<div class="wizard">
						<ul class="steps">
							<li class="complete" data-target="#step1"><span class="badge badge-success">1</span>Atto contabile / allegato<span class="chevron"></span></li>
							<li class="active" data-target="#step2"><span class="badge">2</span>Elenchi collegati<span class="chevron"></span></li>
						</ul>
					</div>

					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<br>
								<div id="accordionElenco" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#divElenchi" data-parent="#accordionElenco" data-toggle="collapse" class="accordion-toggle collapsed">
												Elenchi collegati:
												<span class="accNumInfo" id="numeroElenchiCollegatiAllegatoAtto">
													<s:property value="%{listaElencoDocumentiAllegato.size()}"/>
												</span>
												- Totale Entrate:
												<span class="accNumInfo" id="totaleEntrataAllegatoAtto">
													<s:property value="totaleEntrataListaElencoDocumentiAllegato" />
												</span>
												- Totale Spese:
												<span class="accNumInfo" id="totaleSpesaAllegatoAtto">
													<s:property value="totaleSpesaListaElencoDocumentiAllegato" />
												</span>
												- Totale Netto:
												<span class="accNumInfo" id="totaleNettoAllegatoAtto">
													<s:property value="totaleNettoListaElencoDocumentiAllegato" />
												</span>
												<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="divElenchi">
											<div class="accordion-inner">
												<fieldset class="form-horizontal">
													<table class="table table-hover tab_left" id="tabellaElencoDocumentiAllegato">
														<thead>
															<tr>
																<th></th>
																<th>Elenco</th>
																<th>Stato</th>
																<th>Anno/Numero fonte</th>
																<th>Data trasmissione</th>
																<th>Documenti/Quote</th>
																<th class="tab_Right">Importo Entrate</th>
																<th class="tab_Right">Importo Spese</th>
																<th class="tab_Right">Differenza</th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
												</fieldset>
											</div>
										</div>
									</div>
								</div>
								<div id="dettaglioElementiCollegati" class="collapse">
									<h4>Dettaglio elementi collegati</h4>
									<fieldset class="form-horizontal">
										<table class="table table-hover tab_left" id="tabellaDettaglioElementiCollegati">
											<thead>
												<tr>
													<th>Elenco</th>
													<th>Documento-Quota</th>
													<th>Soggetto</th>
													<th>Movimento</th>
													<th>Capitolo</th>
													<th>Provv. movimento</th>
													<th class="tab_Right">Importo in atto</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</fieldset>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:a cssClass="btn" id="pulsanteRedirezioneIndietro" action="inserisciAllegatoAtto_backToStep1">indietro</s:a>
						<span class="pull-right">
							<a href="inserisciAllegatoAtto_associaMovimento.do" class="btn btn-primary">associa movimento</a>&nbsp;
							<a href="inserisciAllegatoAtto_associaDocumento.do" class="btn btn-primary">associa documento</a>&nbsp;
							<button type="button" class="btn btn-primary" id="pulsanteApriModaleAssociaElencoDocumentiAllegato">associa elenco</button>
						</span>
					</p>
					<s:include value="/jsp/allegatoAtto/associaElencoDocumentiAllegato_modale.jsp" />
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/gestioneElenco.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/inserisci_step2.js"></script>
	
</body>
</html>