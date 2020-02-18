<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="labelModaleSospensione" role="dialog" tabindex="-1" class="modal hide fade" id="modaleSospensione">
	<form action="risultatiRicercaFatturaElettronica_sospendi.do" method="post" novalidate="novalidate" class="modal-form">
		<div class="modal-body">
			<div class="alert alert-error">
				<button data-dismiss="alert" class="close" type="button">&times;</button>
				<p><strong>Attenzione!</strong></p>
				<p><strong>Elemento selezionato: <span id="spanModaleSospensione"></span></strong></p>
				<p>
					Stai per sospendere l'elemento selezionato.
					<br/>
					Sei sicuro di voler proseguire?
				</p>
			</div>
			<div>
				<fieldset class="form-horizontal" id="fieldsetSospensione">
					<div class="control-group">
						<label class="control-label" for="noteFatturaFELModaleSospensione">Note</label>
						<div class="controls">
							<textarea class="span9" cols="15" rows="2" id="noteFatturaFELModaleSospensione" name="fatturaFEL.note"></textarea>
						</div>
					</div>
					<input type="hidden" name="fatturaFEL.idFattura" id="idFatturaFatturaFELModaleSospensione" />
				</fieldset>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
			<button type="submit" class="btn btn-primary">si, prosegui</button>
		</div>
	</form>
</div>