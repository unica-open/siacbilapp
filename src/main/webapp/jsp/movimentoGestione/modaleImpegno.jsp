<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="guidaImpegnoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleImpegno">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="guidaImpegnoLabel">Seleziona impegno</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_IMPEGNO_MODALE">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="FIELDSET_modaleImpegno">
			<div class="control-group">
				<label for="annoImpegnoModale" class="control-label">Anno *</label>
				<div class="controls">
					<input type="text" name="modaleImpegno.impegno.annoMovimento" class="lbTextSmall span2 soloNumeri" id="annoImpegnoModale" required maxlength="4">
					<span class="al">
						<label for="numeroImpegnoModale" class="radio inline">Numero *</label>
					</span>
					<input type="text" name="modaleImpegno.impegno.numero" value="" class="span4 soloNumeri" id="numeroImpegnoModale" required>
					<span class="al">
						<a class="btn btn-primary pull-right" id="pulsanteRicercaImpegnoModale">
							<i class="icon-search icon"></i>cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaImpegnoModale"></i>
						</a>
					</span>
				</div>
			</div>
		</fieldset>
		
		<div id="divImpegniTrovati" class="hide">
			<h4>Elenco elementi trovati</h4>
				
				<table class="table table-hover tab_left" id="tabellaImpegnoModale">
					<thead>
						<tr>
							<th scope="col">Capitolo</th>
							<th scope="col">Provvedimento</th>
							<th scope="col">Soggetto</th>
							<th class="tab_Right" scope="col">Importo</th>
							<th class="tab_Right" scope="col">Disponibile</th>
						</tr>
					</thead>
					<tbody id="bodyTabellaImpegnoModale">
						<tr>
							<td scope="col" id="tabellaImpegno_tdCapitolo"></td>
							<td scope="col" id="tabellaImpegno_tdProvvedimento"></td>
							<td scope="col" id="tabellaImpegno_tdSoggetto"></td>
							<td class="tab_Right whitespace-pre" scope="col" id="tabellaImpegno_tdImporto"></td>
							<td class="tab_Right" scope="col" id="tabellaImpegno_tdDisponibile"></td>
						</tr>
					</tbody>
				</table>
				<table class="table table-hover tab_left" id="tabellaImpegniModale">
					<thead>
						<tr>
							<th scope="col"></th>
							<th scope="col">Numero <abbr title="Subimpegno">Sub.</abbr></th>
							<th scope="col">Descrizione</th>
							<th scope="col">Soggetto</th>
							<th class="tab_Right" scope="col">Importo</th>
							<th class="tab_Right" scope="col">Disponibilit&agrave; a liquidare</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			<div class="Border_line"></div>
			<div id="divMutui" class="accordion" data-overlay>
				<div class="accordion-group">
					<div class="accordion-heading">
						<a href="#collapseMutui" data-parent="#divMutui" data-toggle="collapse" class="accordion-toggle collapsed">
							Mutuo<span class="icon">&nbsp;</span>
						</a>
					</div>
					<div class="accordion-body collapse" id="collapseMutui">
						<div class="accordion-inner">
							<table class="table table-hover tab_left" id="tabellaMutuiModale">
								<thead>
									<tr>
										<th scope="col"></th>
										<th scope="col">Numero Mutuo</th>
										<th scope="col">Descrizione</th>
										<th scope="col">Istituto Mutuante</th>
										<th class="tab_Right" scope="col">Importo Voce</th>
										<th class="tab_Right" scope="col">Disponibilit&agrave; a liquidare</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" id="hidden_ricercaEffettuataConSuccessoModaleImpegno" value="" />
	<input type="hidden" id="hidden_disponibileImpegno" value="" />
	<input type="hidden" id="hidden_cigImpegno" value="" />
	<input type="hidden" id="hidden_cupImpegno" value="" />
	<input type="hidden" id="hidden_flagRilevanteIva" value="" />
	<div class="modal-footer">
		<button class="btn btn-primary" id="pulsanteConfermaModaleImpegno">conferma</button>
	</div>
</div>