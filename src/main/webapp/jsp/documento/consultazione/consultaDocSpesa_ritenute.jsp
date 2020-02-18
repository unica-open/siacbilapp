<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>


<h4 class="step-pane">Ritenute</h4>
<h4>Totale: <s:property value="totaleRitenute"/>
	<span class="alLeft">Importo esente: <s:property value="documento.ritenuteDocumento.importoEsente" default="0,00" /></span>
	<span class="alLeft">Cassa pensioni: <s:property value="documento.ritenuteDocumento.importoCassaPensioni" default="0,00"/></span>
	<span class="alLeft">Rivalsa: <s:property value="documento.ritenuteDocumento.importoRivalsa" default="0,00"/></span>
	<span class="alLeft">Importo IVA: <s:property value="documento.ritenuteDocumento.importoIVA" default="0,00"/></span>
</h4>
<s:if test="%{fatturaFELPresente}">
	<div id="datiFattureDiv" class="step-pane active">
		<div class="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a href="#" class="accordion-toggle collapsed" id="datiFatturePulsante">
						Visualizza dettaglio FEL<span class="icon">&nbsp;</span>
					</a>
				</div>
				<div class="accordion-body collapse" id="datiFattureAccordion">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">
							<table summary="..." class="table table-hover tab_left table-bordered" id="tabellaDatiFatture">
								<thead>
									<tr>
										<th>Numero</th>
										<th><abbr title="Codice esente">Cod. Es</abbr></th>
										<th class="tab_Right">Aliquota &#37;</th>
										<th class="tab_Right">Imponibile</th>
										<th class="tab_Right">Imposta</th>
										<th class="tab_Right">Esente</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
								<tfoot>
									<tr>
										<th class="tab_Center" colspan="3">Totale</th>
										<th data-totale-imponibile class="tab_Right"></th>
										<th data-totale-imposta class="tab_Right"></th>
										<th data-totale-esente class="tab_Right"></th>
									</tr>
								</tfoot>
							</table>
						</fieldset>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:if>

<fieldset class="form-horizontal">
	<h4 class="step-pane">Elenco oneri</h4>
	<table class="table table-hover tab_left" summary="...."  id="tabellaRitenute">
		<thead>
			<tr>
				<th scope="col">Natura</th>
				<th scope="col">Tipologia</th>
				<th class="tab_Right" scope="col">Imponibile</th>
				<th class="tab_Right" scope="col">Aliquota a carico soggetto</th>
				<th class="tab_Right" scope="col">Importo a carico soggetto </th>
				<th class="tab_Right" scope="col">Aliquota a carico Ente</th>
				<th class="tab_Right" scope="col">Importo a carico Ente</th>
				<th class="tab_Right" scope="col">Somma non soggetta</th>
				<th class="tab_Right" scope="col"></th>
			</tr>
		</thead>
		<tbody>
		</tbody>
		<tfoot>
		</tfoot>
	</table>
</fieldset>

<div id="dettRitenute" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettRitenuteLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>Dettaglio ritenute</h4>
	</div>
	<div class="modal-body">
		<div class="boxOrSpan2">
			<div class="boxOrInLeft">
				<p>Onere/Ritenute</p>
				<ul class="htmlelt">
					<li>
						<dfn>Natura Onere</dfn>
						<dl><span id="naturaOnereModaleRitenute"></span></dl>
					</li>
					<li>
						<dfn>Codice Tributo</dfn>
						<dl><span id="codiceTributoModaleRitenute"></span></dl>
					</li>
					<li data-show-natura-onere="SP">
						<dfn>Tipo Split / Reverse</dfn>
						<dl><span id="tipoSplitReverseModaleRitenute"></span></dl>
					</li>
					<li>
						<dfn id="dfnImponibileModaleRitenute">Imponibile</dfn>
						<dl><span id="imponibileModaleRitenute"></span></dl>
					</li>
					<li data-hidden-natura="ES" data-hidden-split-reverse="RC">
						<dfn id="dfnAliquotaSoggettoModaleRitenute">Aliquota a carico soggetto</dfn>
						<dl><span id="aliquotaSoggettoModaleRitenute"></span></dl>
					</li>
					<li data-hidden-natura="ES SP">
						<dfn>Aliquota a carico Ente</dfn>
						<dl><span id="aliquotaEnteModaleRitenute"></span></dl>
					</li>
					<li>
						<dfn id="dfnImportoSoggettoModaleRitenute">Importo a carico soggetto</dfn>
						<dl><span id="importoSoggettoModaleRitenute"></span></dl>
					</li>
					<li data-hidden-natura="ES SP">
						<dfn>Importo a carico Ente</dfn>
						<dl><span id="importoEnteModaleRitenute"></span></dl>
					</li>
					<li data-hidden-natura="ES SP">
						<dfn>Somma non soggetta</dfn>
						<dl><span id="sommaNonModaleRitenute"></span></dl>
					</li>
				</ul>
			</div>

			<div class="boxOrInRight">
				<p>Altri Dati</p>
				<ul class="htmlelt">
					<li data-hidden-natura="ES SP">
						<dfn>Attivit&agrave;</dfn>
						<dl><span id="attivitaModaleRitenute"></span></dl>
					</li>
					<li data-hidden-natura="ES SP">
						<dfn>Periodo</dfn>
						<dl>
							<span id="spanPeriodoRitenute">
								<b>Dal: </b><span id="periodoDalModaleRitenute"></span><span class="alLeft"><b>Al: </b></span><span id="periodoAlModaleRitenute"></span>
							</span>&nbsp;
						</dl>
					</li>
					<li data-hidden-natura="ES SP">
						<dfn>Quadro 770</dfn>
						<dl><span id="quadro770ModaleRitenute"></span></dl>
					</li>
					<li data-hidden-natura="ES SP">
						<dfn>Causale 770</dfn>
						<dl><span id="causale770ModaleRitenute"></span></dl>
					</li>
					<li data-hidden-natura="ES SP">
						<dfn>Codice somma non soggetta</dfn>
						<dl><span id="codiceSommaNonModaleRitenute"></span></dl>
					</li>
					<li>
						<dfn>Reversale</dfn>
						<dl><span id="reversaleModaleRitenute"></span></dl>
					</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="modal-footer">
		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
	</div>

</div>