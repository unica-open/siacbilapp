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
				<s:form action="#" method="post" id="formRisultatiRicercaAllegatoAtto">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Risultati di ricerca atto contabile / allegato</h3>
					<h4 class="step-pane">Dati di ricerca: <s:property value="stringaRiepilogo"/></h4>
					<fieldset class="form-horizontal">
						<h4><span id="id_num_result" class="num_result"></span></h4>
						<table class="table table-hover tab_left dataTable" id="tabellaRisultatiRicercaAllegatoAtto">
							<thead>
								<tr>
									<th class="span1">
										&nbsp;
									</th>
									<th>Provvedimento</th>
									<th></th>
									<th>Sospensione</th>
									<th>Causale</th>
									<th>Data scadenza</th>
									<th>Rich.DURC</th>
									<th>Data fine validita DURC</th>
									<th>Ordinativi</th>
									<th>Stato</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</fieldset>

					<s:include value="/jsp/allegatoAtto/modaleCompletamentoMultiplo.jsp" />
					<s:include value="/jsp/allegatoAtto/convalida/modaleTipoConvalida.jsp" />

					<div class="Border_line"></div>
					
					<s:if test="convalida">
						<div class="accordion-group span4 pull-right" id="accordionConvalida">
							<div class="accordion-heading">
								<a href="#collapseConvalida" data-parent="#accordionConvalida" data-toggle="collapse" class="accordion-toggle collapsed">
									convalida atti contabili / allegati <span class="icon">&nbsp;</span>
								</a>
							</div>
							<div class="accordion-body collapse" id="collapseConvalida">
								<div>
									<ul class="listSelectAccordion">
										<li>
											<a id="pulsanteConvalidaTutti" class="tutti" href="#"><span class="iconSmall icon-chevron-right"></span>tutti</a>
										</li>
										<li>
											<a id="pulsanteConvalidaSelezionati" class="selezionati" href="#"><span class="iconSmall icon-chevron-right"></span>solo selezionati</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</s:if><s:else>				
						<div class="accordion-group span4 pull-right" id="accordionCompletamento">
							<div class="accordion-heading">
								<a href="#collapseCompletamento" data-parent="#accordionDefinisci" data-toggle="collapse" class="accordion-toggle collapsed">
									completa atti contabili / allegati <span class="icon">&nbsp;</span>
								</a>
							</div>
							<div class="accordion-body collapse" id="collapseCompletamento">
								<div>
									<ul class="listSelectAccordion">
										<li>
											<a id="pulsanteCompletaTutti" class="tutti" href="#"><span class="iconSmall icon-chevron-right"></span>tutti</a>
										</li>
										<li>
											<a id="pulsanteCompletaSelezionati" class="selezionati"href="#"><span class="iconSmall icon-chevron-right"></span>solo selezionati</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
						<button type="button" id="pulsanteControllaImporti" class="btn btn-secondary pull-left">controllo importi</button>
					</s:else>
					
					
					
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
					
					<input type="hidden" name="uidAllegatoAtto" id="hiddenUidAllegatoAtto"/>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}allegatoAtto/risultatiRicercaOperazioniMultiple.js"></script>

</body>
</html>