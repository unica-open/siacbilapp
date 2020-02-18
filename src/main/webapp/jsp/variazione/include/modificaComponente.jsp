<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="divComponentiInVariazioneModale" class="hide">
	<div class="accordion_info">
		<div id="headingCollapseComponentiModale" class="accordion-heading">
			<a id="linkCollapseComponentiModale" data-selettore-collapse-interno="#collapseComponentiModale" class="accordion-toggle gestisci-collapse" href="#" data-ignore-collapse>
				Componenti <span class="icon"></span>
			</a>
		</div>
		<div id="collapseComponentiModale" class="hide">
				<div class="control-group">
				<br/>
					<button type="button" class="btn pull-left gestisci-collapse" data-selettore-collapse-interno="#collapseNuovaComponenteModale" id="buttonNuovaComponenteModale">nuova componente</button>
				</div>
				<div id="collapseNuovaComponenteModale" class="hide">
					<fieldset class="form-horizontal">
						<div class="control-group">
							<label class="control-label" for="listaTipoComponenteModale">Componente</label>
							<div class="controls">
								<s:select list="listaTipoComponente" cssClass="span3" id="listaTipoComponenteModale" name="tipoComponente.uid"
								headerKey="0" headerValue="" listKey="uid" listValue="%{descrizione}" />
								<span class="al">
									<label class="radio inline" for="tipoComponenteVariazioneModale">Tipo</label>
								</span>
								<s:textfield id="tipoComponenteVariazioneModale" data-maintain="" cssClass="lbTextSmall span3" value="Stanziamento" disabled="true"  name="dettaglio.tipoComponente" />
								<span class="al">
									<label class="radio inline" for="componenteInVariazioneDaEliminareModale">Da eliminare</label>
								</span>
								<input type="checkbox" id="componenteInVariazioneDaEliminareModale" name="dettaglio.DaEliminare"/>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="importoComponenteAnno0InVariazioneModale"> Importo ${annoEsercizioInt}</label>
							<div class="controls">
								<s:textfield id="importoComponenteAnno0InVariazioneModale" cssClass="lbTextSmall span2 decimale soloNumeri" name="dettaglio.importo0" />
								<span class="al">
									<label class="radio inline" for="importoComponenteAnno1InVariazioneModale">Importo ${annoEsercizioInt +1} </label>
								</span>
								<s:textfield id="importoComponenteAnno1InVariazioneModale" cssClass="lbTextSmall span2 decimale soloNumeri" name="dettaglio.importo" />
								<span class="al">
									<label class="radio inline" for="importoComponenteAnno2InVariazioneModale">Importo ${annoEsercizioInt +2} </label>
								</span>
								<s:textfield id="importoComponenteAnno2InVariazioneModale" cssClass="lbTextSmall span2 decimale soloNumeri" name="dettaglio.importo" />
								&nbsp; <button type="button" class="btn" id="button_salvaNuovaComponenteModale">aggiungi</button><%-- Gestione della chiamata AJAX --%>
							</div>
						</div>
					</fieldset>
				</div>
				<div class="clear"></div>
				<div class="span11">
					<table class="table table-hover table-bordered table-condensed tab_left" id="tabellaStanziamentiTotaliPerComponenteModale">
						<thead>
							<tr>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th class="span3"><abbr title="componente da eliminare sul capitolo">Da eliminare sul capitolo</abbr></th>
								<th class="span3">Importo ${annoEsercizioInt}</th>
								<th class="span3">Importo ${annoEsercizioInt +1}</th>
								<th class="span3">Importo ${annoEsercizioInt +2}</th>
								<th class="tab_Right span1">&nbsp;</th>
								<th class="tab_Right span1">&nbsp;</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	<div class="clear"></div>
	<h5>Stanziamenti</h5>
</div>