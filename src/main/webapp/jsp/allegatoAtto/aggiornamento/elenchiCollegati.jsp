<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:form id="formAggiornaAllegatoAttoElenchi" cssClass="form-horizontal" novalidate="novalidate" action="#" method="post">
	<s:hidden id="HIDDEN_parzialmenteConvalidato" name="parzialmenteConvalidato" />
	<s:hidden id="HIDDEN_anno_datepicker" name="annoEsercizioInt" />
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
										<th></th>
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
		<div id="dettaglioElementiCollegati" class="hide">
			<h4>Dettaglio elementi collegati</h4>
			<fieldset class="form-horizontal">
				<table class="table table-hover tab_left" id="tabellaDettaglioElementiCollegati">
					<thead>
						<tr>
							<th>Elenco</th>
							<th>Documento-Quota</th>
							<th><abbr title="Modalità pagamento soggetto">Mod.Pag.</abbr></th>
							<th>Soggetto</th>
							<th>Movimento</th>
							<th>Capitolo</th>
							<th><abbr title="Provvedimento">Provv.</abbr> movimento</th>
							<th>IVA</th>
							<th>Annotazioni</th>
							<th class="tab_Right">Importo in atto</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
			<s:include value="/jsp/allegatoAtto/aggiornamento/include/ricercaQuoteNellElenco_aggiornamento.jsp"/>
		</div>
	</fieldset>
	<p class="margin-medium">
		<s:include value="/jsp/include/indietro.jsp" />
		<span class="pull-right">
			<a href="aggiornaAllegatoAtto_associaMovimento.do" class="btn btn-primary">associa movimento</a>&nbsp;
			<a href="aggiornaAllegatoAtto_associaDocumento.do" class="btn btn-primary">associa documento</a>&nbsp;
			<button type="button" class="btn btn-primary" id="pulsanteApriModaleAssociaElencoDocumentiAllegato">associa elenco</button>
		</span>
	</p>
	<s:include value="/jsp/allegatoAtto/associaElencoDocumentiAllegato_modale.jsp" />
	<s:include value="/jsp/include/modaleConfermaEliminazione.jsp" />
</s:form>
<div id="containerModaleAggiornamentoQuotaElenco"></div>
<div id="containerSlideSpezzaQuotaElenco" class="hide"></div>
<s:include value="/jsp/provvisorioCassa/modaleRicercaProvvisorioCassa.jsp" />