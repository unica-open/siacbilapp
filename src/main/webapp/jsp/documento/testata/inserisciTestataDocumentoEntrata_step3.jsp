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
					<h3>Inserimento documenti iva entrata</h3>
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
											<p>Documenti di entrata</p>
											<ul class="htmlelt">
												<li>
													<dfn>Tipo</dfn>
													<dl><s:property value="documento.tipoDocumento.descrizione" />&nbsp;</dl>
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
											</ul>
										</div>

										<div class="boxOrInRight">
											<p>Altri dati</p>
											<ul class="htmlelt">
												<li>
													<dfn>Importi</dfn>
													<dl><s:property value="documento.importo" /></dl>
												</li>
												<li>
													<dfn>Descrizione</dfn>
													<dl><s:property value="documento.descrizione" />&nbsp;</dl>
												</li>
												<li>
													<dfn>Data scadenza</dfn>
													<dl><s:property value="documento.dataScadenza" />&nbsp;</dl>
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
						<s:submit cssClass="btn btn-secondary" action="inserisciTestataDocumentoEntrata_aggiorna" value="aggiorna" />
						<s:submit cssClass="btn btn-secondary" action="inserisciTestataDocumentoEntrata_ripeti" value="ripeti" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>