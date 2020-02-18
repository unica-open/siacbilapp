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
				<s:form cssClass="form-horizontal">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserimento documenti spesa</h3>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li class="complete" data-target="#step1"><span class="badge badge-success">1</span>dati principali<span class="chevron"></span></li>
							<li class="complete" data-target="#step2"><span class="badge badge-success">2</span>dettaglio<span class="chevron"></span></li>
							<li class="active" data-target="#step3"><span class="badge">3</span>riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step3" class="step-pane active">
							<h4>Riepilogo Dati</h4>
							<div class="PaddcontentPage">
								<h4 class="nostep-pane">
									Stato: <s:property value="documento.statoOperativoDocumento" />
								</h4>
								<fieldset class="form-horizontal">
									<div class="boxOrSpan2">
										<div class="boxOrInLeft">
											<p>Documenti di spesa</p>
											<ul class="htmlelt">
												<li>
													<dfn>Tipo</dfn>
													<dl><s:property value="documento.tipoDocumento.descrizione" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Tipo documento siope</dfn>
													<dl><s:property value="documento.siopeDocumentoTipo.codice"/> - <s:property value="documento.siopeDocumentoTipo.descrizione"/>&nbsp;</dl>
												</li>
												<li>
													<dfn>Tipo documento analogico siope</dfn>
													<dl><s:property value="documento.siopeDocumentoTipoAnalogico.codice"/> - <s:property value="documento.siopeDocumentoTipoAnalogico.descrizione"/>&nbsp;</dl>
												</li>
												<li>
													<dfn>Identificativo lotto SDI</dfn>
													<dl><s:property value="documento.siopeIdentificativoLottoSdi"/>&nbsp;</dl>
												</li>
												<li>
													<dfn>Anno</dfn>
													<dl><s:property value="documento.anno" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Numero</dfn>
													<dl><s:property value="documento.numero" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Data</dfn>
													<dl><s:property value="documento.dataEmissione"/>&nbsp;</dl>
												</li>
												<li>
													<dfn>Codice</dfn>
													<dl><s:property value="soggetto.codiceSoggetto" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Denominazione</dfn>
													<dl><s:property value="soggetto.denominazione" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Codice fiscale</dfn>
													<dl><s:property value="soggetto.codiceFiscale" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Partita IVA</dfn>
													<dl><s:property value="soggetto.partitaIva" />&nbsp;</dl>
												</li>
												<li>
													<dfn><abbr title="Struttura Amministrativo Contabile">Strutt.Amm.Cont.</abbr></dfn>
													<dl><s:property value="stringaDescrizioneSAC" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Beneficiario multiplo</dfn>
													<dl>
														<s:if test="documento.flagBeneficiarioMultiplo">
															s&iacute;
														</s:if><s:else>
															no
														</s:else>
													</dl>
												</li>
											</ul>
										</div>

										<div class="boxOrInRight">
											<p>Altri dati</p>
											<ul class="htmlelt">
												<li>
													<dfn>Importi</dfn>
													<dl>
														<b>Documento: </b>
														<span class="datiIns"><s:property value="documento.importo" /></span>
														<span>-</span>
														<b>Arrotondamento: </b>
														<span class="datiIns"><s:property value="documento.arrotondamento" /></span>
														<span>-</span>
														<b>Netto: </b>
														<span class="datiIns"><s:property value="netto" /></span>
													</dl>
												</li>
												<li>
													<dfn>Descrizione</dfn>
													<dl><s:property value="documento.descrizione" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Termine di pagamento</dfn>
													<dl><s:property value="documento.terminePagamento" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Data scadenza</dfn>
													<dl><s:property value="documento.dataScadenza" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Dati repertorio/protocollo</dfn>
													<dl>
														<b>Registro: </b>
														<span class="datiIns"><s:property value="documento.registroRepertorio" /></span>
														<span>-</span>
														<b>Anno: </b>
														<span class="datiIns"><s:property value="documento.annoRepertorio" /></span>
														<span>&nbsp;</span>
														<s:if test="%{documento.numeroRepertorio != null}">
															<b>Numero: </b>
															<span class="datiIns"><s:property value="documento.numeroRepertorio" /></span>
															<span>-</span>
															<b>Data: </b>
															<span class="datiIns"><s:property value="documento.dataRepertorio" /></span>
														</s:if>&nbsp;
													</dl>
												</li>
												<!-- SIAC-6840 -->
 												<li>
 													<dfn>Codice Avviso Pago PA</dfn>
 													<dl><s:property value="documento.codAvvisoPagoPA" />&nbsp;</dl>
 												</li>
												<!-- SIAC-6840 -->
												<li>
													<dfn>Soggetto pignorato</dfn>
													<dl><s:property value="documento.codiceFiscalePignorato" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Codice bollo</dfn>
													<dl><s:property value="documento.codiceBollo.codice" />&nbsp;-&nbsp;<s:property value="documento.codiceBollo.descrizione" /></dl>
												</li>
												<li>
													<dfn>Tipo impresa</dfn>
													<dl>
														<s:if test="%{documento.tipoImpresa != null}">
															<s:property value="documento.tipoImpresa.codice" />&nbsp;-&nbsp;<s:property value="documento.tipoImpresa.descrizione" />
														</s:if>&nbsp;
													</dl>
												</li>
												<li>
													<dfn>Note</dfn>
													<dl><s:property value="documento.note" />&nbsp;</dl>
												</li>
											</ul>
										</div>
									</div>
								</fieldset>
							</div>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:submit cssClass="btn btn-secondary" action="inserisciDocumentoSpesa_aggiorna" value="aggiorna" />
						<s:submit cssClass="btn btn-secondary" action="inserisciDocumentoSpesa_ripeti" value="ripeti" />
						<s:submit cssClass="btn btn-primary pull-right" action="inserisciDocumentoSpesa_quote" value="quote" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>