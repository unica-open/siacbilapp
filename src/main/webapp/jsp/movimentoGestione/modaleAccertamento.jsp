<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="guidaAccertamentoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAccertamento">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="guidaAccertamentoLabel">Seleziona accertamento</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_ACCERTAMENTO_MODALE">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="FIELDSET_modaleAccertamento">
			<div class="control-group">
				<label for="annoAccertamentoModale" class="control-label">Anno *</label>
				<div class="controls">
					<input type="text" name="modaleAccertamento.accertamento.annoMovimento" class="lbTextSmall span2 soloNumeri" id="annoAccertamentoModale" required maxlength="4">
					<span class="al">
						<label for="numeroAccertamentoModale" class="radio inline">Numero *</label>
					</span>
					<input type="text" name="modaleAccertamento.accertamento.numero" value="" class="lbTextSmall span2 soloNumeri" id="numeroAccertamentoModale" required>
					<span class="al">
						<a class="btn btn-primary pull-right" id="pulsanteRicercaAccertamentoModale">
							<i class="icon-search icon"></i>cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaAccertamentoModale"></i>
						</a>
					</span>
				</div>
			</div>
		</fieldset>
		
		<div id="divAccertamentiTrovati" class="hide">
			<h4>Elenco elementi trovati</h4>
			
			<table class="table table-hover tab_left" id="tabellaAccertamentoModale">
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
						<td scope="col" id="tabellaAccertamento_tdCapitolo"></td>
						<td scope="col" id="tabellaAccertamento_tdProvvedimento"></td>
						<td scope="col" id="tabellaAccertamento_tdSoggetto"></td>
						<td class="tab_Right whitespace-pre" scope="col" id="tabellaAccertamento_tdImporto"></td>
						<td class="tab_Right" scope="col" id="tabellaAccertamento_tdDisponibile"></td>
					</tr>
				</tbody>
			</table>
			<table summary="...." class="table table-hover tab_left" id="tabellaAccertamentiModale">
				<thead>
					<tr>
						<th scope="col"></th>
						<th scope="col">Numero <abbr title="Subaccertamento">Sub.</abbr></th>
						<th scope="col">Descrizione</th>
						<th scope="col">Debitore</th>
						<th class="tab_Right" scope="col">Importo</th>
						<th class="tab_Right" scope="col">Disponibile</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div class="Border_line"></div>
		
		</div>
	</div>
	<input type="hidden" id="hidden_ricercaEffettuataConSuccessoModaleAccertamento" value="" />
	<input type="hidden" id="hidden_disponibileAccertamento" value="" />
	<input type="hidden" id="hidden_flagRilevanteIva" value="" />
	<div class="modal-footer">
		<button class="btn btn-primary" id="pulsanteConfermaModaleAccertamento">conferma</button>
	</div>
</div>