<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div aria-hidden="true" aria-labelledby="labelAssociaElencoDocumentiAllegato_modale" role="dialog" tabindex="-1"
		class="modal hide fade" id="modaleAssociaElencoDocumentiAllegato">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="labelAssociaElencoDocumentiAllegato_modale">Seleziona elenco</h4>
		<p>&Eacute; necessario inserire almeno due criteri di ricerca.</p>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal">
			<div class="accordion-body collapse in" id="campiRicercaElencoDocumentiAllegato_modale">
				<div class="alert alert-error hide" id="ERRORI_ElencoDocumentiAllegato">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
					<ul></ul>
				</div>
				<fieldset id="fieldsetElencoDocumentiAllegato_modale">
					<div class="control-group">
						<label class="control-label" for="annoElencoDocumentiAllegato_modale">Anno</label>
						<div class="controls">
							<s:textfield id="annoElencoDocumentiAllegato_modale" name="modale.elencoDocumentiAllegato.anno" maxlength="7"
								cssClass="span2 soloNumeri" value="%{annoEsercizioInt}" />
							<span class="alRight">
								<label class="radio inline" for="numeroElencoDocumentiAllegato_modale">Numero</label>
							</span>
							<s:textfield id="numeroElencoDocumentiAllegato_modale" name="modale.elencoDocumentiAllegato.numero"
								maxlength="7" cssClass="span2 soloNumeri" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">Fonte dati</label>
						<div class="controls">
							<s:textfield id="annoSysEsternoElencoDocumentiAllegato_modale" name="modale.elencoDocumentiAllegato.annoSysEsterno"
									maxlength="7" cssClass="span2 soloNumeri" placeholder="%{'Anno'}" />
							<s:textfield id="numeroSysEsternoElencoDocumentiAllegato_modale" name="modale.elencoDocumentiAllegato.numeroSysEsterno"
									maxlength="500" cssClass="span3" placeholder="%{'Numero'}" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="dataTrasmissioneElencoDocumentiAllegato_modale">Data trasmissione</label>
						<div class="controls">
							<s:textfield id="dataTrasmissioneElencoDocumentiAllegato_modale" name="modale.elencoDocumentiAllegato.dataTrasmissione"
								maxlength="10" cssClass="span3 datepicker" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="statoOperativoElencoDocumentiElencoDocumentiAllegato_modale">Stato</label>
						<div class="controls">
							<s:select list="listaStatoOperativoElencoDocumenti" name="modale.elencoDocumentiAllegato.statoOperativoElencoDocumenti"
								id="statoOperativoElencoDocumentiElencoDocumentiAllegato_modale" listValue="%{codice + ' - ' + descrizione}"
								headerKey="" headerValue="" cssClass="span9" />
						</div>
					</div>
				</fieldset>
			</div>
			<button type="button" class="btn btn-primary pull-right" id="pulsanteRicercaElencoDocumentiAllegato_modale">
				<i class="icon-search icon" id="iconaRicercaElencoDocumentiAllegato_modale"></i>&nbsp;cerca
			</button>
			<div class="clear"></div>
			<div id="divRisultatiElencoDocumentiAllegato_modale" class="collapse">
				<h4>Elenco elementi trovati</h4>
				<table class="table table-hover tab_left" id="tabellaRisultatiElencoDocumentiAllegato_modale">
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
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-primary" id="pulsanteConfermaElencoDocumentiAllegato_modale">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaElencoDocumentiAllegato_modale"></i>
		</button>
	</div>
</div>