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
					<h3>Ricerca registro prime note definitive verso inventario contabile</h3>
					<p>&Eacute; necessario selezionare il tipo e inserire almeno un criterio di ricerca.</p>
					<s:include value="/jsp/include/messaggi.jsp" />
					<div class="step-content">
						<div class="step-pane active">
							<s:form id="formRicercaRegistroA" cssClass="form-horizontal" novalidate="novalidate" action="ricercaRegistroACespite_effettuaRicerca">
								<s:hidden name="ambito" id="ambito" />
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label">Tipo prima nota</label>
										<div class="controls">
											<s:iterator value="listaTipoCausale" var="tc">
												<label class="radio inline">
													<input
														name="primaNota.tipoCausale"
														type="radio"
														value="<s:property value="#tc"/>"
														data-pn-selector="<s:property value="#tc.codice"/>"
														<s:if test="primaNota.tipoCausale == #tc">checked</s:if>/> <s:property value="#tc.descrizione" /> &nbsp;
												</label>
											</s:iterator>
											<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
										</div>
									</div>
									<s:include value="/jsp/cespiti/registroa/ricercaRegistroA_libera.jsp" />
									<s:include value="/jsp/cespiti/registroa/ricercaRegistroA_integrata.jsp" />
								</fieldset>
								<p class="margin-medium">
									<s:include value="/jsp/include/indietro.jsp" />
									<button type="button" class="btn reset">annulla</button>
									<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
								</p>
							</s:form>
							<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
							<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaContoFIN.jsp" />
							<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
							<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
							<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp">
								<s:param name="suffix">_cug</s:param>
							</s:include>
							<s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp">
								<s:param name="suffix">_ceg</s:param>
							</s:include>
							<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
							<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaContoFIN.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale_doc.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/registroa/ricercaRegistroA.js"></script>
</body>
</html>